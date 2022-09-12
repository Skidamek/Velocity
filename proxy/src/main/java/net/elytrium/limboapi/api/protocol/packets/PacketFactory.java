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

import net.elytrium.limboapi.api.protocol.packets.data.AbilityFlags;
import net.elytrium.limboapi.api.chunk.Dimension;
import net.elytrium.limboapi.api.chunk.data.ChunkSnapshot;
import net.elytrium.limboapi.api.material.VirtualItem;
import net.elytrium.limboapi.api.protocol.packets.data.MapData;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface PacketFactory {

  Object createChangeGameStatePacket(int reason, float value);

  Object createChunkDataPacket(ChunkSnapshot chunkSnapshot, boolean legacySkyLight, int maxSections);

  Object createChunkDataPacket(ChunkSnapshot chunkSnapshot, Dimension dimension);

  Object createDefaultSpawnPositionPacket(int posX, int posY, int posZ, float angle);

  Object createMapDataPacket(int mapID, byte scale, MapData mapData);

  /**
   * @param flags See {@link AbilityFlags}. (e.g. {@code AbilityFlags.ALLOW_FLYING | AbilityFlags.CREATIVE_MODE})
   */
  Object createPlayerAbilitiesPacket(byte flags, float flySpeed, float walkSpeed);

  Object createPositionRotationPacket(double posX, double posY, double posZ, float yaw, float pitch,
      boolean onGround, int teleportID, boolean dismountVehicle);

  Object createSetExperiencePacket(float expBar, int level, int totalExp);

  Object createSetSlotPacket(int windowID, int slot, VirtualItem item, int count, int data, @Nullable CompoundBinaryTag nbt);

  Object createTimeUpdatePacket(long worldAge, long timeOfDay);

  Object createUpdateViewPositionPacket(int posX, int posZ);
}
