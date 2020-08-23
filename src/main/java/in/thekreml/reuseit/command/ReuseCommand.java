package in.thekreml.reuseit.command;

import in.thekreml.reuseit.ReuseIt;
import in.thekreml.reuseit.config.UserPreferences;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class ReuseCommand implements CommandExecutor {
  private final ReuseIt plugin;

  public ReuseCommand(ReuseIt plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
    plugin.getLog().fine("Handling command " + label);

    if (!(sender instanceof Player)) {
      return false;
    }

    final Player player = (Player)sender;
    final boolean newValue = UserPreferences.toggle(player.getName());

    if (newValue) {
      player.sendMessage("\u00a7b[Reuse-It] \u00a7a\u00a7nACTIVE");
    } else {
      player.sendMessage("\u00a7b[Reuse-It] \u00a7c\u00a7nINACTIVE");
    }

    return true;
  }
}
