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

package net.elytrium.limboapi.api.chunk;

import java.util.List;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.common.value.qual.IntRange;

public interface VirtualWorld {

  @NonNull
  VirtualBlock getBlock(int posX, int posY, int posZ);

  void setBiome2d(int posX, int posZ, @NonNull VirtualBiome biome);

  void setBiome3d(int posX, int posY, int posZ, @NonNull VirtualBiome biome);

  VirtualBiome getBiome(int posX, int posY, int posZ);

  byte getBlockLight(int posX, int posY, int posZ);

  void setBlockLight(int posX, int posY, int posZ, byte light);

  void fillBlockLight(@IntRange(from = 0, to = 15) int level);

  void fillSkyLight(@IntRange(from = 0, to = 15) int level);

  List<VirtualChunk> getChunks();

  @Nullable
  VirtualChunk getChunk(int posX, int posZ);

  VirtualChunk getChunkOrNew(int posX, int posZ);

  @NonNull
  Dimension getDimension();

  double getSpawnX();

  double getSpawnY();

  double getSpawnZ();

  float getYaw();

  float getPitch();

  void setBlock(int posX, int posY, int posZ, @Nullable VirtualBlock block);
}
