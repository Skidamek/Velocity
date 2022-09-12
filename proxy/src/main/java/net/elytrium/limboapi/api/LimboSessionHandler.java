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

package net.elytrium.limboapi.api;

import net.elytrium.limboapi.api.player.LimboPlayer;

public interface LimboSessionHandler {

  default void onSpawn(Limbo server, LimboPlayer player) {

  }

  default void onMove(double posX, double posY, double posZ) {

  }

  default void onMove(double posX, double posY, double posZ, float yaw, float pitch) {

  }

  default void onRotate(float yaw, float pitch) {

  }

  default void onGround(boolean onGround) {

  }

  default void onTeleport(int teleportID) {

  }

  default void onChat(String chat) {

  }

  /**
   * @param packet Any velocity built-in packet or any packet registered via {@link LimboFactory#registerPacket}.
   */
  default void onGeneric(Object packet) {

  }

  default void onDisconnect() {

  }
}
