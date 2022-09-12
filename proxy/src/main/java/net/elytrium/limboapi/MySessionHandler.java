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

package net.elytrium.limboapi;

import com.velocitypowered.api.command.CommandInvocation;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.proxy.auth.VelocityAuth;
import com.velocitypowered.proxy.auth.commands.Command;
import net.elytrium.limboapi.api.Limbo;
import net.elytrium.limboapi.api.LimboSessionHandler;
import net.elytrium.limboapi.api.player.LimboPlayer;
import net.elytrium.limboapi.server.LimboImpl;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;

public class MySessionHandler implements LimboSessionHandler {
    public LimboPlayer player;
    public LimboImpl server;
    @Override
    public void onSpawn(Limbo server, LimboPlayer player) {
        this.server = (LimboImpl) server;
        this.player = player;
        player.disableFalling();
    }

    @Override
    public void onChat(String chat) {
        if(chat.trim().isEmpty()) return;
        if(!chat.startsWith("/")) return;

        boolean hasSpaces = chat.contains(" ");
        String commandName;
        if(hasSpaces) commandName = chat.substring(1, chat.indexOf(" ")); // Start at 1 to exclude first /
        else commandName = chat.substring(1); // Start at 1 to exclude first /

        if(commandName.isEmpty()) return;

        for (Command command : server.myCommands) {
            if(command.name().equals(commandName)){
                try{
                    String[] rawArgs = chat.split(" ");
                    String[] args = Arrays.copyOfRange(rawArgs, 1, rawArgs.length);
                    command.execute(new SimpleCommand.Invocation(){

                        @Override
                        public String alias() {
                            return null;
                        }

                        @Override
                        public CommandSource source() {
                            return player.getProxyPlayer();
                        }

                        @Override
                        public String @NonNull [] arguments() {
                            return args;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
