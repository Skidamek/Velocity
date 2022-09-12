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

package net.elytrium.limboapi.api.protocol;

import com.velocitypowered.api.network.ProtocolVersion;
import java.util.List;
import java.util.function.Function;

public interface PreparedPacket {

  <T> PreparedPacket prepare(T packet);

  <T> PreparedPacket prepare(T[] packets);

  <T> PreparedPacket prepare(List<T> packets);

  <T> PreparedPacket prepare(T packet, ProtocolVersion from);

  <T> PreparedPacket prepare(T packet, ProtocolVersion from, ProtocolVersion to);

  <T> PreparedPacket prepare(T[] packets, ProtocolVersion from);

  <T> PreparedPacket prepare(T[] packets, ProtocolVersion from, ProtocolVersion to);

  <T> PreparedPacket prepare(List<T> packets, ProtocolVersion from);

  <T> PreparedPacket prepare(List<T> packets, ProtocolVersion from, ProtocolVersion to);

  <T> PreparedPacket prepare(Function<ProtocolVersion, T> packet);

  <T> PreparedPacket prepare(Function<ProtocolVersion, T> packet, ProtocolVersion from);

  <T> PreparedPacket prepare(Function<ProtocolVersion, T> packet, ProtocolVersion from, ProtocolVersion to);

  PreparedPacket build();

  void release();
}
