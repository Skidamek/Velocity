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

package net.elytrium.limboapi.api.chunk.data;

import com.velocitypowered.api.network.ProtocolVersion;
import net.elytrium.limboapi.api.chunk.VirtualBlock;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface BlockStorage {

  void write(Object byteBufObject, ProtocolVersion version);

  void set(int posX, int posY, int posZ, @NonNull VirtualBlock block);

  @NonNull
  VirtualBlock get(int posX, int posY, int posZ);

  int getDataLength(ProtocolVersion version);

  BlockStorage copy();

  static int index(int posX, int posY, int posZ) {
    return posY << 8 | posZ << 4 | posX;
  }
}
