package com.bulletlogic.reuseit.command;

import com.bulletlogic.reuseit.ReuseIt;
import com.bulletlogic.reuseit.config.UserPreferences;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReuseCommand implements CommandExecutor {
  private final ReuseIt plugin;

  public ReuseCommand(ReuseIt plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
    plugin.getLog().fine("Handling command " + label);

    if (!(sender instanceof final Player player)) {
      return false;
    }

    final boolean newValue = UserPreferences.toggle(player.getName());

    if (newValue) {
      player.sendMessage("\u00a7b[Reuse-It] \u00a7a\u00a7nACTIVE");
    } else {
      player.sendMessage("\u00a7b[Reuse-It] \u00a7c\u00a7nINACTIVE");
    }

    return true;
  }
}
