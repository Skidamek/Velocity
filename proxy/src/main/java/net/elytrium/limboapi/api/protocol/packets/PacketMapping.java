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

import com.velocitypowered.api.network.ProtocolVersion;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PacketMapping {

  private final int id;
  private final ProtocolVersion protocolVersion;
  @Nullable
  private final ProtocolVersion lastValidProtocolVersion;
  private final boolean encodeOnly;

  public PacketMapping(int id, ProtocolVersion protocolVersion, boolean encodeOnly) {
    this(id, protocolVersion, null, encodeOnly);
  }

  public PacketMapping(int id, ProtocolVersion protocolVersion, @Nullable ProtocolVersion lastValidProtocolVersion, boolean encodeOnly) {
    this.id = id;
    this.protocolVersion = protocolVersion;
    this.lastValidProtocolVersion = lastValidProtocolVersion;
    this.encodeOnly = encodeOnly;
  }

  public int getID() {
    return this.id;
  }

  public ProtocolVersion getProtocolVersion() {
    return this.protocolVersion;
  }

  @Nullable
  public ProtocolVersion getLastValidProtocolVersion() {
    return this.lastValidProtocolVersion;
  }

  public boolean isEncodeOnly() {
    return this.encodeOnly;
  }
}
