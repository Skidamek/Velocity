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

package net.elytrium.limboapi.api;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.proxy.Player;
import net.elytrium.limboapi.api.command.LimboCommandMeta;
import net.elytrium.limboapi.api.player.GameMode;

public interface Limbo {

  void spawnPlayer(Player player, LimboSessionHandler handler);

  void respawnPlayer(Player player);

  long getCurrentOnline();

  Limbo setName(String name);

  Limbo setReadTimeout(int millis);

  Limbo setWorldTime(long ticks);

  Limbo setGameMode(GameMode gameMode);

  Limbo setShouldRespawn(boolean shouldRespawn);

  Limbo setMaxSuppressPacketLength(int maxSuppressPacketLength);

  Limbo registerCommand(LimboCommandMeta commandMeta);

  Limbo registerCommand(CommandMeta commandMeta, Command command);

  void dispose();
}
