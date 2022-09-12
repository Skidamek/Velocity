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
 *  You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package net.elytrium.limboapi.api.event;

import com.google.common.base.Preconditions;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.proxy.Player;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

/**
 * This event is fired during login process before the player has been authenticated, e.g. to enable or disable custom authentication.
 */
public class LoginLimboRegisterEvent {

  private final Player player;
  private final Queue<Runnable> onJoinCallbacks;
  private Function<KickedFromServerEvent, Boolean> onKickCallback;

  public LoginLimboRegisterEvent(Player player) {
    this.player = Preconditions.checkNotNull(player, "player");
    this.onJoinCallbacks = new LinkedBlockingQueue<>();
  }

  public Player getPlayer() {
    return this.player;
  }

  @Override
  public String toString() {
    return "LoginLimboRegisterEvent{"
        + "player=" + this.player
        + "}";
  }

  public Queue<Runnable> getOnJoinCallbacks() {
    return this.onJoinCallbacks;
  }


  public Function<KickedFromServerEvent, Boolean> getOnKickCallback() {
    return this.onKickCallback;
  }

  public void addOnJoinCallback(Runnable callback) {
    this.onJoinCallbacks.add(callback);
  }

  /**
   * @deprecated Use {@link LoginLimboRegisterEvent#addOnJoinCallback(Runnable)} instead
   */
  @Deprecated
  public void addCallback(Runnable callback) {
    this.onJoinCallbacks.add(callback);
  }

  public void setOnKickCallback(Function<KickedFromServerEvent, Boolean> callback) {
    this.onKickCallback = callback;
  }
}
