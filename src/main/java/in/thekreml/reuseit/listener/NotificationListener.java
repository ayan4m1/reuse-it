package in.thekreml.reuseit.listener;

import in.thekreml.reuseit.config.UserPreferences;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class NotificationListener implements Listener {
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    final Player player = event.getPlayer();

    if (UserPreferences.isEnabled(player.getName())) {
      player.sendMessage("\u00a7b[Reuse-It] \u00a7a\u00a7nACTIVE");
    } else {
      player.sendMessage("\u00a7b[Reuse-It] \u00a7c\u00a7nINACTIVE");
    }
  }
}
