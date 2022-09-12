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

package net.elytrium.limboapi.api.protocol.packets.data;

/**
 * For MapData packet.
 */
public class MapData {

  public static final int MAP_DIM_SIZE = 128;
  public static final int MAP_SIZE = MAP_DIM_SIZE * MAP_DIM_SIZE; // 128² == 16384

  private final int columns;
  private final int rows;
  private final int posX;
  private final int posY;
  private final byte[] data;

  public MapData(byte[] data) {
    this(0, data);
  }

  public MapData(int posX, byte[] data) {
    this(MAP_DIM_SIZE, MAP_DIM_SIZE, posX, 0, data);
  }

  public MapData(int columns, int rows, int posX, int posY, byte[] data) {
    this.columns = columns;
    this.rows = rows;
    this.posX = posX;
    this.posY = posY;
    this.data = data;
  }

  public int getColumns() {
    return this.columns;
  }

  public int getRows() {
    return this.rows;
  }

  public int getX() {
    return this.posX;
  }

  public int getY() {
    return this.posY;
  }

  public byte[] getData() {
    return this.data;
  }

  @Override
  public String toString() {
    return "MapData{"
        + "columns=" + this.columns
        + ", rows=" + this.rows
        + ", posX=" + this.posX
        + ", posY=" + this.posY
        + "}";
  }
}
