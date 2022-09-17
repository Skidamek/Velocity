/*
 * Copyright (C) 2018 Velocity Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.velocitypowered.proxy.connection.client;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Longs;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent.PreLoginComponentResult;
import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.proxy.crypto.IdentifiedKey;
import com.velocitypowered.api.util.GameProfile;
import com.velocitypowered.proxy.VelocityServer;
import com.velocitypowered.proxy.auth.VelocityAuth;
import com.velocitypowered.proxy.auth.utils.GetJson;
import com.velocitypowered.proxy.connection.MinecraftConnection;
import com.velocitypowered.proxy.connection.MinecraftSessionHandler;
import com.velocitypowered.proxy.crypto.IdentifiedKeyImpl;
import com.velocitypowered.proxy.protocol.netty.MinecraftDecoder;
import com.velocitypowered.proxy.protocol.packet.EncryptionRequest;
import com.velocitypowered.proxy.protocol.packet.EncryptionResponse;
import com.velocitypowered.proxy.protocol.packet.LoginPluginResponse;
import com.velocitypowered.proxy.protocol.packet.ServerLogin;
import io.netty.buffer.ByteBuf;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.net.UrlEscapers.urlFormParameterEscaper;
import static com.velocitypowered.proxy.VelocityServer.GENERAL_GSON;
import static com.velocitypowered.proxy.connection.VelocityConstants.EMPTY_BYTE_ARRAY;
import static com.velocitypowered.proxy.crypto.EncryptionUtils.decryptRsa;
import static com.velocitypowered.proxy.crypto.EncryptionUtils.generateServerId;

public class InitialLoginSessionHandler implements MinecraftSessionHandler {

  private static final Logger logger = LogManager.getLogger(InitialLoginSessionHandler.class);
  private static final String MOJANG_HASJOINED_URL =
      System.getProperty("mojang.sessionserver", "https://sessionserver.mojang.com/session/minecraft/hasJoined")
          .concat("?username=%s&serverId=%s");

  private final VelocityServer server;
  private final MinecraftConnection mcConnection;
  private final LoginInboundConnection inbound;
  private @MonotonicNonNull ServerLogin login;
  private byte[] verify = EMPTY_BYTE_ARRAY;
  private LoginState currentState = LoginState.LOGIN_PACKET_EXPECTED;
  private boolean forceKeyAuthentication;

  InitialLoginSessionHandler(VelocityServer server, MinecraftConnection mcConnection,
                             LoginInboundConnection inbound) {
    this.server = Preconditions.checkNotNull(server, "server");
    this.mcConnection = Preconditions.checkNotNull(mcConnection, "mcConnection");
    this.inbound = Preconditions.checkNotNull(inbound, "inbound");
    this.forceKeyAuthentication = System.getProperties().containsKey("auth.forceSecureProfiles")
            ? Boolean.getBoolean("auth.forceSecureProfiles") : server.getConfiguration().isForceKeyAuthentication();
  }

  @Override
  public boolean handle(ServerLogin packet) {
    assertState(LoginState.LOGIN_PACKET_EXPECTED);
    this.currentState = LoginState.LOGIN_PACKET_RECEIVED;
    IdentifiedKey playerKey = packet.getPlayerKey();
    if (playerKey != null) {
      if (playerKey.hasExpired()) {
        inbound.disconnect(Component.translatable("multiplayer.disconnect.invalid_public_key_signature"));
        return true;
      }

      boolean isKeyValid;
      if (playerKey.getKeyRevision() == IdentifiedKey.Revision.LINKED_V2
              && playerKey instanceof IdentifiedKeyImpl) {
        IdentifiedKeyImpl keyImpl = (IdentifiedKeyImpl) playerKey;
        isKeyValid = keyImpl.internalAddHolder(packet.getHolderUuid());
      } else {
        isKeyValid = playerKey.isSignatureValid();
      }

      if (!isKeyValid) {
        inbound.disconnect(Component.translatable("multiplayer.disconnect.invalid_public_key"));
        return true;
      }
    } else if (mcConnection.getProtocolVersion().compareTo(ProtocolVersion.MINECRAFT_1_19) >= 0
            && forceKeyAuthentication) {
      inbound.disconnect(Component.translatable("multiplayer.disconnect.missing_public_key"));
      return true;
    }
    inbound.setPlayerKey(playerKey);
    this.login = packet;

    PreLoginEvent event = new PreLoginEvent(inbound, login.getUsername());
    server.getEventManager().fire(event)
        .thenRunAsync(() -> {
          if (mcConnection.isClosed()) {
            // The player was disconnected
            return;
          }

          PreLoginComponentResult result = event.getResult();
          Optional<Component> disconnectReason = result.getReasonComponent();
          if (disconnectReason.isPresent()) {
            // The component is guaranteed to be provided if the connection was denied.
            inbound.disconnect(disconnectReason.get());
            return;
          }

          inbound.loginEventFired(() -> {
            if (mcConnection.isClosed()) {
              // The player was disconnected
              return;
            }

            if (mcConnection.getProtocolVersion().compareTo(ProtocolVersion.MINECRAFT_1_19_1) >= 0) {
              // 1.19.1 and above
              // online-mode players: 100% OK
              // offline-mode players: 100% OK
              mcConnection.eventLoop().execute(() -> {
                if(packet.getHolderUuid() == null){ // Probably a connection from an offline/cracked player
                  if (server.getConfiguration().isOnlineMode()) {
                    inbound.disconnect(Component.text("This server does not allow offline-mode players."));
                    return;
                  }
                  mcConnection.setSessionHandler(new AuthSessionHandler(
                          server, inbound, GameProfile.forOfflinePlayer(login.getUsername()), false
                  ));
                }
                else { // Probably a connection from an online/regular player
                  EncryptionRequest request = generateEncryptionRequest();
                  this.verify = Arrays.copyOf(request.getVerifyToken(), 4);
                  mcConnection.write(request);
                  this.currentState = LoginState.ENCRYPTION_REQUEST_SENT;
                }
              });
            } else {
              // 1.19.0 and below
              // online-mode players: 100% OK
              // offline-mode players: 99% OK
              // Work on all Minecraft clients, some will get "Invalid Session" error and directly disconnect from server,
              // if they are not premium players with nickname from some premium account the encryption request was sent.
              mcConnection.eventLoop().execute(() -> {
                byte[] oldVerify = (this.verify != null ? Arrays.copyOf(this.verify, this.verify.length) : null);
                LoginState oldState = this.currentState;

                // Check the player's nickname to mojang api if that exists we can assume that the player has premium account and use premium authentication
                String USERNAME = login.getUsername();
                JsonElement MinecraftProfile = getMinecraftProfile(USERNAME);

                if (MinecraftProfile != null) {
                  String API_UUID = MinecraftProfile.get("id").getAsString();
                  String API_USERNAME = MinecraftProfile.get("name").getAsString();

                  if (!API_UUID.equals("null")) {
                    if (USERNAME.equals(API_USERNAME)) {
                      // Player for 99% has premium account, if not, they must use nickname which is not registered on mojang api, otherwise they would get "Invalid Session" error
                      // So we can use premium authentication
                      EncryptionRequest request = generateEncryptionRequest();
                      this.verify = Arrays.copyOf(request.getVerifyToken(), 4);
                      mcConnection.write(request);
                      this.currentState = LoginState.ENCRYPTION_REQUEST_SENT;
                      return;
                    }
                  }
                }

                // Now we know that the player is 100% offline player, we can use offline authentication
                if (!server.getConfiguration().isOnlineMode()){
                  VelocityAuth.INSTANCE.executor.execute(() -> {
                    try {
                      mcConnection.eventLoop().execute(() -> {
                        this.verify = oldVerify;
                        this.currentState = oldState;
                        mcConnection.setSessionHandler(new AuthSessionHandler(
                                server, inbound, GameProfile.forOfflinePlayer(login.getUsername()), false
                        ));
                      });
                    } catch (Exception e) {
                      logger.error("Exception in pre-login stage", e);
                    }
                  });
                }
              });
            }
          });
        }, mcConnection.eventLoop())
        .exceptionally((ex) -> {
          logger.error("Exception in pre-login stage", ex);
          return null;
        });

    return true;
  }

  String API_URL_MOJANG = "https://api.mojang.com/users/profiles/minecraft/";
  String API_URL_MINETOOLS = "https://api.minetools.eu/uuid/";

  JsonElement getMinecraftProfile(String USERNAME) {
    JsonElement JSON = null;

    API_URL_MOJANG = API_URL_MOJANG + USERNAME;
    API_URL_MINETOOLS = API_URL_MINETOOLS + USERNAME;

    // get Json Array from autoplug core
    try {
      // TODO get JSON from API_URL_MOJANG
    } catch (Exception e) { // Ignore if player doesn't have premium account, always will throw exception
    }

    if (JSON == null) {
      try {
        // TODO get JSON from API_URL_MINETOOLS
      } catch (Exception e) { // Means that both APIs are down
        ru.nanit.limbo.server.Logger.error("Exception when trying to get a Minecraft profile for " + USERNAME);
      }
    }

    return JSON;
  }

  @Override
  public boolean handle(LoginPluginResponse packet) {
    this.inbound.handleLoginPluginResponse(packet);
    return true;
  }

  @Override
  public boolean handle(EncryptionResponse packet) {
    assertState(LoginState.ENCRYPTION_REQUEST_SENT);
    this.currentState = LoginState.ENCRYPTION_RESPONSE_RECEIVED;
    ServerLogin login = this.login;
    if (login == null) {
      throw new IllegalStateException("No ServerLogin packet received yet.");
    }

    ru.nanit.limbo.server.Logger.info("Test log 1");

    if (verify.length == 0) {
      throw new IllegalStateException("No EncryptionRequest packet sent yet.");
    }

    ru.nanit.limbo.server.Logger.info("Test log 2");

    try {
      KeyPair serverKeyPair = server.getServerKeyPair();
      if (inbound.getIdentifiedKey() != null) {
        IdentifiedKey playerKey = inbound.getIdentifiedKey();
        if (!playerKey.verifyDataSignature(packet.getVerifyToken(), verify, Longs.toByteArray(packet.getSalt()))) {
          throw new IllegalStateException("Invalid client public signature.");
        }
      } else {
        byte[] decryptedVerifyToken = decryptRsa(serverKeyPair, packet.getVerifyToken());
        if (!MessageDigest.isEqual(verify, decryptedVerifyToken)) {
          throw new IllegalStateException("Unable to successfully decrypt the verification token.");
        }
      }

      ru.nanit.limbo.server.Logger.warning("Test log 3");

      byte[] decryptedSharedSecret = decryptRsa(serverKeyPair, packet.getSharedSecret());
      String serverId = generateServerId(decryptedSharedSecret, serverKeyPair.getPublic());

      String playerIp = ((InetSocketAddress) mcConnection.getRemoteAddress()).getHostString();
      String url = String.format(MOJANG_HASJOINED_URL,
          urlFormParameterEscaper().escape(login.getUsername()), serverId);

      if (server.getConfiguration().shouldPreventClientProxyConnections()) {
        url += "&ip=" + urlFormParameterEscaper().escape(playerIp);
      }

      ru.nanit.limbo.server.Logger.warning("Test log 4");

      ListenableFuture<Response> hasJoinedResponse = server.getAsyncHttpClient().prepareGet(url)
          .execute();
      hasJoinedResponse.addListener(() -> {
        if (mcConnection.isClosed()) {
          // The player disconnected after we authenticated them.
          return;
        }

        // Go ahead and enable encryption. Once the client sends EncryptionResponse, encryption
        // is enabled.
        try {
          mcConnection.enableEncryption(decryptedSharedSecret);
        } catch (GeneralSecurityException e) {
          logger.error("Unable to enable encryption for connection", e);
          // At this point, the connection is encrypted, but something's wrong on our side and
          // we can't do anything about it.
          mcConnection.close(true);
          return;
        }

        ru.nanit.limbo.server.Logger.warning("Test log 5");

        try {
          Response profileResponse = hasJoinedResponse.get();
          if (profileResponse.getStatusCode() == 200) {
            final GameProfile profile = GENERAL_GSON.fromJson(profileResponse.getResponseBody(), GameProfile.class);
            ru.nanit.limbo.server.Logger.warning("Test log 6");
            // Not so fast, now we verify the public key for 1.19.1+
            if (inbound.getIdentifiedKey() != null
                    && inbound.getIdentifiedKey().getKeyRevision() == IdentifiedKey.Revision.LINKED_V2
                    && inbound.getIdentifiedKey() instanceof IdentifiedKeyImpl) {
              IdentifiedKeyImpl key = (IdentifiedKeyImpl) inbound.getIdentifiedKey();
              if (!key.internalAddHolder(profile.getId())) {
                inbound.disconnect(Component.translatable("multiplayer.disconnect.invalid_public_key"));
              }
            }

            ru.nanit.limbo.server.Logger.warning("Test log 7");

            // All went well, initialize the session.
            mcConnection.setSessionHandler(new AuthSessionHandler(
                server, inbound, profile, true
            ));
          } else if (profileResponse.getStatusCode() == 204) {
            // Apparently an offline-mode user logged onto this online-mode proxy.
            inbound.disconnect(Component.translatable("velocity.error.online-mode-only",
                NamedTextColor.RED));
          } else {
            // Something else went wrong
            logger.error(
                "Got an unexpected error code {} whilst contacting Mojang to log in {} ({})",
                profileResponse.getStatusCode(), login.getUsername(), playerIp);
            inbound.disconnect(Component.translatable("multiplayer.disconnect.authservers_down"));
          }

          ru.nanit.limbo.server.Logger.warning("Test log 8");

        } catch (ExecutionException e) {
          logger.error("Unable to authenticate with Mojang", e);
          inbound.disconnect(Component.translatable("multiplayer.disconnect.authservers_down"));
        } catch (InterruptedException e) {
          // not much we can do usefully
          Thread.currentThread().interrupt();
        }
      }, mcConnection.eventLoop());
    } catch (GeneralSecurityException e) {
      logger.error("Unable to enable encryption", e);
      mcConnection.close(true);
    }
    return true;
  }

  private EncryptionRequest generateEncryptionRequest() {
    byte[] verify = new byte[4];
    ThreadLocalRandom.current().nextBytes(verify);

    EncryptionRequest request = new EncryptionRequest();
    request.setPublicKey(server.getServerKeyPair().getPublic().getEncoded());
    request.setVerifyToken(verify);
    return request;
  }

  @Override
  public void handleUnknown(ByteBuf buf) {
    mcConnection.close(true);
  }

  @Override
  public void disconnected() {
    this.inbound.cleanup();
  }

  private void assertState(LoginState expectedState) {
    if (this.currentState != expectedState) {
      if (MinecraftDecoder.DEBUG) {
        logger.error("{} Received an unexpected packet requiring state {}, but we are in {}", inbound,
            expectedState, this.currentState);
      }
      mcConnection.close(true);
    }
  }

  private enum LoginState {
    LOGIN_PACKET_EXPECTED,
    LOGIN_PACKET_RECEIVED,
    ENCRYPTION_REQUEST_SENT,
    ENCRYPTION_RESPONSE_RECEIVED
  }
}
