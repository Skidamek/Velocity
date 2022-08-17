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

package com.velocitypowered.proxy.connection;

import com.velocitypowered.proxy.protocol.MinecraftPacket;
import com.velocitypowered.proxy.protocol.packet.*;
import com.velocitypowered.proxy.protocol.packet.chat.*;
import com.velocitypowered.proxy.protocol.packet.title.*;
import io.netty.buffer.ByteBuf;

public interface MinecraftSessionHandler {

  default boolean beforeHandle() {
    return false;
  }

  default void handleGeneric(MinecraftPacket packet) {

  }

  default void handleUnknown(ByteBuf buf) {

  }

  default void connected() {

  }

  default void disconnected() {

  }

  default void activated() {

  }

  default void deactivated() {

  }

  default void exception(Throwable throwable) {

  }

  default void writabilityChanged() {

  }

  default void readCompleted() {

  }

  default boolean handle(AvailableCommands commands) {
    return false;
  }

  default boolean handle(BossBar packet) {
    return false;
  }

  default boolean handle(LegacyChat packet) {
    return false;
  }

  default boolean handle(ClientSettings packet) {
    return false;
  }

  default boolean handle(Disconnect packet) {
    return false;
  }

  default boolean handle(EncryptionRequest packet) {
    return false;
  }

  default boolean handle(EncryptionResponse packet) {
    return false;
  }

  default boolean handle(Handshake packet) {
    return false;
  }

  default boolean handle(HeaderAndFooter packet) {
    return false;
  }

  default boolean handle(JoinGame packet) {
    return false;
  }

  default boolean handle(KeepAlive packet) {
    return false;
  }

  default boolean handle(LegacyHandshake packet) {
    return false;
  }

  default boolean handle(LegacyPing packet) {
    return false;
  }

  default boolean handle(LoginPluginMessage packet) {
    return false;
  }

  default boolean handle(LoginPluginResponse packet) {
    return false;
  }

  default boolean handle(PluginMessage packet) {
    return false;
  }

  default boolean handle(Respawn packet) {
    return false;
  }

  default boolean handle(ServerLogin packet) {
    return false;
  }

  default boolean handle(ServerLoginSuccess packet) {
    return false;
  }

  default boolean handle(SetCompression packet) {
    return false;
  }

  default boolean handle(StatusPing packet) {
    return false;
  }

  default boolean handle(StatusRequest packet) {
    return false;
  }

  default boolean handle(StatusResponse packet) {
    return false;
  }

  default boolean handle(TabCompleteRequest packet) {
    return false;
  }

  default boolean handle(TabCompleteResponse packet) {
    return false;
  }

  default boolean handle(LegacyTitlePacket packet) {
    return false;
  }

  default boolean handle(TitleTextPacket packet) {
    return false;
  }

  default boolean handle(TitleSubtitlePacket packet) {
    return false;
  }

  default boolean handle(TitleActionbarPacket packet) {
    return false;
  }

  default boolean handle(TitleTimesPacket packet) {
    return false;
  }

  default boolean handle(TitleClearPacket packet) {
    return false;
  }

  default boolean handle(PlayerListItem packet) {
    return false;
  }

  default boolean handle(ResourcePackRequest packet) {
    return false;
  }

  default boolean handle(ResourcePackResponse packet) {
    return false;
  }

  default boolean handle(PlayerChat packet) {
    return false;
  }

  default boolean handle(SystemChat packet) {
    return false;
  }

  default boolean handle(ServerPlayerChat packet) {
    return false;
  }

  default boolean handle(PlayerChatPreview packet) {
    return false;
  }

  default boolean handle(ServerChatPreview packet) {
    return false;
  }

  default boolean handle(PlayerCommand packet) {
    return false;
  }

  default boolean handle(PlayerChatCompletion packet) {
    return false;
  }
  
  default boolean handle(ServerData serverData) {
    return false;
  }
}
