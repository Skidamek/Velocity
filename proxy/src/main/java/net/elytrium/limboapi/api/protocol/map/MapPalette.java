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

package net.elytrium.limboapi.api.protocol.map;

import com.velocitypowered.api.network.ProtocolVersion;
import java.awt.image.BufferedImage;

@Deprecated(forRemoval = true)
public class MapPalette {

  public static int[] imageToBytes(BufferedImage image) {
    return net.elytrium.limboapi.api.protocol.packets.data.MapPalette.imageToBytes(image);
  }

  public static int[] imageToBytes(BufferedImage image, ProtocolVersion version) {
    return net.elytrium.limboapi.api.protocol.packets.data.MapPalette.imageToBytes(image, version);
  }

  public static byte tryFastMatchColor(int rgb, ProtocolVersion version) {
    return net.elytrium.limboapi.api.protocol.packets.data.MapPalette.tryFastMatchColor(rgb, version);
  }
}
