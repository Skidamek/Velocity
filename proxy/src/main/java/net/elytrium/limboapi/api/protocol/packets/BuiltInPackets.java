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

package net.elytrium.limboapi.api.protocol.packets;

import net.elytrium.java.commons.reflection.ReflectionException;

@Deprecated(forRemoval = true)
public enum BuiltInPackets {

  ChangeGameState("net.elytrium.limboapi.protocol.packets.s2c.ChangeGameStatePacket"),
  ChunkData("net.elytrium.limboapi.protocol.packets.s2c.ChunkDataPacket"),
  DefaultSpawnPosition("net.elytrium.limboapi.protocol.packets.s2c.DefaultSpawnPositionPacket"),
  MapData("net.elytrium.limboapi.protocol.packets.s2c.MapDataPacket"),
  PlayerAbilities("net.elytrium.limboapi.protocol.packets.s2c.PlayerAbilitiesPacket"),
  PlayerPositionAndLook("net.elytrium.limboapi.protocol.packets.s2c.PositionRotationPacket"),
  SetExperience("net.elytrium.limboapi.protocol.packets.s2c.SetExperiencePacket"),
  SetSlot("net.elytrium.limboapi.protocol.packets.s2c.SetSlotPacket"),
  TimeUpdate("net.elytrium.limboapi.protocol.packets.s2c.TimeUpdatePacket"),
  UpdateViewPosition("net.elytrium.limboapi.protocol.packets.s2c.UpdateViewPositionPacket");

  private final Class<?> packetClass;

  BuiltInPackets(String packetClass) {
    try {
      this.packetClass = Class.forName(packetClass);
    } catch (ClassNotFoundException e) {
      throw new ReflectionException(e);
    }
  }

  public Class<?> getPacketClass() {
    return this.packetClass;
  }
}
