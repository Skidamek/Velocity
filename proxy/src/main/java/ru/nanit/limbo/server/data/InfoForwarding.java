/*
 * Copyright (C) 2020 Nan1t
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
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ru.nanit.limbo.server.data;

import java.util.ArrayList;
import java.util.List;

public class InfoForwarding {

    public Type type;
    public String secret;
    public ArrayList<String> tokens;

    public Type getType() {
        return type;
    }

    public String getSecret() {
        return secret;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public boolean hasToken(String token) {
        return tokens != null && token != null && tokens.contains(token);
    }

    public boolean isNone() {
        return type == Type.NONE;
    }

    public boolean isLegacy() {
        return type == Type.LEGACY;
    }

    public boolean isModern() {
        return type == Type.MODERN;
    }

    public boolean isBungeeGuard() {
        return type == Type.BUNGEE_GUARD;
    }

    public enum Type {
        NONE,
        LEGACY,
        MODERN,
        BUNGEE_GUARD
    }
}
