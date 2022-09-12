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

package net.elytrium.limboapi.api.file;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import net.elytrium.limboapi.api.LimboFactory;
import net.elytrium.limboapi.api.chunk.VirtualWorld;
import net.kyori.adventure.nbt.BinaryTagIO;
import net.kyori.adventure.nbt.CompoundBinaryTag;

public class SchematicFile implements WorldFile {

  private short width;
  private short height;
  private short length;
  private byte[] blocks;
  private byte[] addBlocks = new byte[0];
  //private byte[] blocksData;

  public SchematicFile(Path file) throws IOException {
    this.fromNBT(BinaryTagIO.unlimitedReader().read(file, BinaryTagIO.Compression.GZIP));
  }

  public SchematicFile(InputStream stream) throws IOException {
    this.fromNBT(BinaryTagIO.unlimitedReader().read(stream, BinaryTagIO.Compression.GZIP));
  }

  public SchematicFile(CompoundBinaryTag tag) {
    this.fromNBT(tag);
  }

  private void fromNBT(CompoundBinaryTag tag) {
    this.width = tag.getShort("Width");
    this.height = tag.getShort("Height");
    this.length = tag.getShort("Length");
    this.blocks = tag.getByteArray("Blocks");
    //this.blocksData = tag.getByteArray("Data");

    if (tag.keySet().contains("AddBlocks")) {
      this.addBlocks = tag.getByteArray("AddBlocks");
    }
  }

  @Override
  public void toWorld(LimboFactory factory, VirtualWorld world, int offsetX, int offsetY, int offsetZ) {
    this.toWorld(factory, world, offsetX, offsetY, offsetZ, 15);
  }

  @Override
  public void toWorld(LimboFactory factory, VirtualWorld world, int offsetX, int offsetY, int offsetZ, int lightLevel) {
    short[] blockIDs = new short[this.blocks.length];
    for (int index = 0; index < blockIDs.length; ++index) {
      if ((index >> 1) >= this.addBlocks.length) {
        blockIDs[index] = (short) (this.blocks[index] & 0xFF);
      } else {
        if ((index & 1) == 0) {
          blockIDs[index] = (short) (((this.addBlocks[index >> 1] & 0x0F) << 8) + (this.addBlocks[index] & 0xFF));
        } else {
          blockIDs[index] = (short) (((this.addBlocks[index >> 1] & 0xF0) << 4) + (this.addBlocks[index] & 0xFF));
        }
      }
    }

    for (int posX = 0; posX < this.width; ++posX) {
      for (int posY = 0; posY < this.height; ++posY) {
        for (int posZ = 0; posZ < this.length; ++posZ) {
          int index = (posY * this.length + posZ) * this.width + posX;
          world.setBlock(posX + offsetX, posY + offsetY, posZ + offsetZ, factory.createSimpleBlock(blockIDs[index]));
        }
      }
    }

    world.fillSkyLight(lightLevel);
  }
}
