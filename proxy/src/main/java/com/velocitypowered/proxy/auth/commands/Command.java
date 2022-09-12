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

package com.velocitypowered.proxy.auth.commands;


import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.proxy.auth.VelocityAuth;

public interface Command extends SimpleCommand {
    String name();

    String[] aliases();

    String permission();

    default void register() {
        CommandManager commandManager = VelocityAuth.INSTANCE.proxy.getCommandManager();
        commandManager.register(meta(), this);
    }

    @Override
    default boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission(permission());
    }

    /**
     * Actual execution code in here (independent from velocity api).
     *
     * @param args arguments.
     * @return error message, null if no error.
     * @throws Exception if something went really wrong.
     */
    String execute(Object... args) throws Exception;

    default CommandMeta meta(){
        CommandManager commandManager = VelocityAuth.INSTANCE.proxy.getCommandManager();
        return commandManager.metaBuilder(name()).aliases(aliases()).build();
    }
}
