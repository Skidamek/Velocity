# Velocity

A Minecraft server proxy with unparalleled server support, scalability,
and flexibility.

## Usage
1. Download the velocity jar from the latest release [here](https://github.com/Osiris-Team/Velocity/releases).
2. Stop your proxy and replace the old velocity jar with the new/downloaded one.
3. Make sure your velocity config has
`player-info-forwarding-mode = "modern"`
and `force-key-authentication = false`
set, to work properly.
4. Make sure all your minecraft servers have `online-mode=false` in their `server.properties`.
5. Done! Start your velocity proxy (`java -jar velocity.jar` in command line or script).

It's recommended to also install the [ViaVersion](https://www.spigotmc.org/resources/viaversion.19254/) 
and [SkinsRestorer](https://www.spigotmc.org/resources/skinsrestorer.2124/)
plugins on your proxy (to add support for more Minecraft versions and 
restore the players skins).

## Features
This velocity fork makes it possible for you
to enable offline mode and not fear about
offline-mode (cracked) players causing you trouble.

Your server will be available to a much larger player-base,
and you should see a fast increase in player count.

Premium players will **not** have to register/login, only
offline-mode players must.

- **Basics**
  - SQL database **required** (normally pre-installed on your system).
  - Ban system, to block players from joining your proxy.
  - Whitelist mode to completely block not registered players from joining.

- **Security against offline-mode players**
  - Register/Login **required** (session based authentication, players only need to login once).
  - Connections to other servers are blocked, if the player is not logged in, and  get automatically forwarded to the limbo auth-server (in spectator mode).
  - Blocks all proxy command execution for not logged in players (except the /register and /login commands)
    , by changing the permissions function of the player.
  - Prevents kicking of already connected players (via username spoofing).
  - Prevents join blocking (via username spoofing).

- **Security for online-mode players**
  - Register/Login **is not required**
  - If a offline-mode player was playing on the server before
  and had the same name as the current online-mode player that joined, the
  offline-mode player loses access to the account.

- **Registration/Login**
  - Only happens on a pre-configured, auto-installed and auto-started limbo auth-server.
  - Secured against password timing attacks.
  - Secured against password spamming attacks, by temp-banning those players (configurable).
  - Secured against SQL injection.

- **Other changes**
  - `help or ?` will display a list of all available velocity commands.

## Player commands

#### /register _password_ _confirm-password_
- `velocityauth.register`
- Players have this permission by default when not logged in.
- Registers the player.

#### /login _password_
- `velocityauth.login`
- Players have this permission by default when not logged in.
- The minimum password length is 10 (configurable).
- Logins the player. On success, forwards the player to the first server, restores permissions, and creates a session
  so this player can rejoin without needing to login again.
  Failed logins get saved to a table, together with
  the UUID and IP of the player. If there are more than 5 failed attempts (for the same UUID OR IP)
  in the last hour, the player gets banned for 10 seconds on each
  following failed attempt.

## Admin commands

#### /a_register _username_ _password_
- `velocityauth.admin.register`
- Registers the provided player.

#### /a_unregister _username_
- `velocityauth.admin.unregister`
- Unregisters the provided player.

#### /a_login _username_ _password_
- `velocityauth.admin.login`
- Logins the provided player.

#### /ban _username_ (_hours_) (_reason_)
- `velocityauth.ban`
- Bans the player for 24h, with default reason: Your behavior violated our community guidelines and/or terms of service.
  The UUID and IP of the player gets added to
  the banned players table. On each player join that table gets
  checked and if there is a match for the UUID OR IP,
  the connection is aborted.

#### /unban _username_
- `velocityauth.unban`
- Unbans the player, by setting the ban expires timestamp to the current time.

#### /list_sessions _(username)_
- `velocityauth.list.sessions`
- Lists all sessions, or the sessions for a specific player.

#### /clear_sessions _(username)_
- `velocityauth.clear.sessions`
- Removes/Clears all sessions from the database, or the sessions for a specific player.


## Developers

#### Goals

* A codebase that is easy to dive into and consistently follows best practices
  for Java projects as much as reasonably possible.
* High performance: handle thousands of players on one proxy.
* A new, refreshing API built from the ground up to be flexible and powerful
  whilst avoiding design mistakes and suboptimal designs from other proxies.
* First-class support for Paper, Sponge, and Forge. (Other implementations
  may work, but we make every endeavor to support these server implementations
  specifically.)
  
#### Building

Velocity is built with [Gradle](https://gradle.org). We recommend using the
wrapper script (`./gradlew`) as our CI builds using it.

It is sufficient to run `./gradlew build` to run the full build cycle.

#### Running

Once you've built Velocity, you can copy and run the `-all` JAR from
`proxy/build/libs`. Velocity will generate a default configuration file
and you can configure it from there.

Alternatively, you can get the proxy JAR from the [downloads](https://papermc.io/downloads#Velocity)
page.
