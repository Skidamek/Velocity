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

package net.elytrium.limboapi.api.player;

import org.checkerframework.checker.nullness.qual.Nullable;

public enum GameMode {

  SURVIVAL,
  CREATIVE,
  ADVENTURE,
  SPECTATOR;

  /**
   * Cached {@link #values()} array to avoid constant array allocation.
   */
  private static final GameMode[] VALUES = values();

  /**
   * Get the ID of this {@link GameMode}.
   *
   * @return The ID.
   *
   * @see #getByID(int)
   */
  public short getID() {
    return (short) this.ordinal();
  }

  /**
   * Get a {@link GameMode} by its' ID.
   *
   * @param id The ID.
   *
   * @return The {@link GameMode}, or {@code null} if it does not exist.
   *
   * @see #getID()
   */
  @Nullable
  public static GameMode getByID(int id) {
    return VALUES[id];
  }
}
