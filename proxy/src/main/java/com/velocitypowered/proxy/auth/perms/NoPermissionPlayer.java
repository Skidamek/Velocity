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

package com.velocitypowered.proxy.auth.perms;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.proxy.auth.commands.LoginCommand;
import com.velocitypowered.proxy.auth.commands.RegisterCommand;

import java.util.Objects;
import java.util.function.Predicate;

public class NoPermissionPlayer {
    public static final String registerPermission = new RegisterCommand().permission();
    public static final String loginPermission = new LoginCommand().permission();
    /**
     * Temporary permission function that gets set if the player is not logged in.
     * Only allows /register and /login commands of VelocityAuth to be executed.
     */
    public static final Predicate<String> tempPermissionFunction =
            permission -> Objects.equals(registerPermission, permission) ||
                    Objects.equals(loginPermission, permission);
    /**
     * Player that has blocked permissions.
     */
    public Player player;
    public MutablePermissionProvider permissionProvider;
    /**
     * Old permission function that is used,
     * to restore permissions after a successful login.
     */
    public Predicate<String> oldPermissionFunction;

    public NoPermissionPlayer(Player player, MutablePermissionProvider permissionProvider, Predicate<String> oldPermissionFunction) {
        this.player = player;
        this.permissionProvider = permissionProvider;
        this.oldPermissionFunction = oldPermissionFunction;
    }
}
