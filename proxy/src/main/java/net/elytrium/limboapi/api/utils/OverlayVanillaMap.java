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

package net.elytrium.limboapi.api.utils;

import com.google.common.collect.Streams;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class OverlayVanillaMap<K, V> extends OverlayMap<K, V> {

  public OverlayVanillaMap(Map<K, V> parent, Map<K, V> overlay) {
    super(parent, overlay);
  }

  @Override
  public Set<K> keySet() {
    return Streams.concat(this.parent.keySet().stream(), this.overlay.keySet().stream()).collect(Collectors.toSet());
  }

  @NotNull
  @Override
  public Collection<V> values() {
    return Streams.concat(this.parent.values().stream(), this.overlay.values().stream()).collect(Collectors.toList());
  }

  @NotNull
  @Override
  public Set<Entry<K, V>> entrySet() {
    return Streams.concat(this.parent.entrySet().stream(), this.overlay.entrySet().stream()).collect(Collectors.toSet());
  }
}
