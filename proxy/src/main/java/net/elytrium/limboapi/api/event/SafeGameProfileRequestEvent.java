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

package net.elytrium.limboapi.api.event;

import com.google.common.base.Preconditions;
import com.velocitypowered.api.util.GameProfile;

/**
 * Safe GameProfileRequestEvent, which executes only after all the login limbo server.
 *
 * @deprecated Use Velocity built-in GameProfileRequestEvent
 */
@Deprecated
public class SafeGameProfileRequestEvent {

  private final String username;
  private final GameProfile originalProfile;
  private final boolean onlineMode;
  private GameProfile gameProfile;

  public SafeGameProfileRequestEvent(GameProfile originalProfile, boolean onlineMode) {
    this.originalProfile = Preconditions.checkNotNull(originalProfile, "originalProfile");
    this.username = originalProfile.getName();
    this.onlineMode = onlineMode;
  }

  public String getUsername() {
    return this.username;
  }

  public GameProfile getOriginalProfile() {
    return this.originalProfile;
  }

  public boolean isOnlineMode() {
    return this.onlineMode;
  }

  public void setGameProfile(GameProfile gameProfile) {
    this.gameProfile = gameProfile;
  }

  public GameProfile getGameProfile() {
    return this.gameProfile == null ? this.originalProfile : this.gameProfile;
  }

  public String toString() {
    return "SafeGameProfileRequestEvent{"
        + "username=" + this.username
        + ", originalProfile=" + this.originalProfile
        + ", gameProfile=" + this.gameProfile
        + "}";
  }
}
