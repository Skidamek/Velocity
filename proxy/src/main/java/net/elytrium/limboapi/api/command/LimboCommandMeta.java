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

package net.elytrium.limboapi.api.command;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.tree.CommandNode;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import java.util.Collection;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class LimboCommandMeta implements CommandMeta {

  @NonNull
  private final Collection<String> aliases;
  @NonNull
  private final Collection<CommandNode<CommandSource>> hints;
  @Nullable // Why?..
  private final Object plugin;

  public LimboCommandMeta(@NonNull Collection<String> aliases) {
    this(aliases, null, null);
  }

  public LimboCommandMeta(@NonNull Collection<String> aliases, @Nullable Collection<CommandNode<CommandSource>> hints) {
    this(aliases, hints, null);
  }

  public LimboCommandMeta(@NonNull Collection<String> aliases, @Nullable Collection<CommandNode<CommandSource>> hints, @Nullable Object plugin) {
    this.aliases = aliases;
    this.hints = Objects.requireNonNullElse(hints, ImmutableList.of());
    this.plugin = plugin;
  }

  @NonNull
  @Override
  public Collection<String> getAliases() {
    return this.aliases;
  }

  @NonNull
  @Override
  public Collection<CommandNode<CommandSource>> getHints() {
    return this.hints;
  }

  @Nullable
  @Override
  public Object getPlugin() {
    return this.plugin;
  }
}
