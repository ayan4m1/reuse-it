package in.thekreml.reuseit.listener;

import in.thekreml.reuseit.Constants;
import in.thekreml.reuseit.ReuseIt;
import in.thekreml.reuseit.utils.SwapUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PlayerListener implements Listener {
  private static final Set<Material> THROWN_ITEMS =
      new HashSet<>(Arrays.asList(Material.EGG, Material.SNOWBALL, Material.EXPERIENCE_BOTTLE));

  private final ReuseIt plugin;

  public PlayerListener(ReuseIt plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    final Player player = event.getPlayer();

    if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }

    final Block block = event.getClickedBlock();
    if (block == null) {
      return;
    }

    checkStack(player, event.getItem());
  }

  @EventHandler
  public void onPlayerBreakItem(PlayerItemBreakEvent event) {
    checkStack(event.getPlayer(), event.getBrokenItem());
  }

  @EventHandler
  public void onPlayerConsume(PlayerItemConsumeEvent event) {
    checkStack(event.getPlayer(), event.getItem());
  }

  @EventHandler
  public void onProjectileLaunch(ProjectileLaunchEvent event) {
    final Projectile entity = event.getEntity();

    if (!(entity.getShooter() instanceof Player)) {
      return;
    }

    final Player player = (Player)entity.getShooter();
    final ItemStack stack = player.getInventory().getItemInMainHand();

    checkStack(player, stack);
  }

  private void checkStack(Player player, ItemStack item) {
    if (player.getGameMode() != GameMode.SURVIVAL) {
      return;
    }

    if (item == null || item.getData() == null) {
      return;
    }

    if (item.getAmount() != 1) {
      return;
    }

    final String itemType = item.getData().getItemType().name();

    if (!plugin.getConfigModel().getMaterials().contains(item.getData().getItemType())) {
      plugin.getLog().fine(itemType + " is not eligible for reuse!");
      return;
    }

    if (!plugin.getPermissions().has(player, Constants.PERMISSION_USE)) {
      plugin.getLog().info(player.getName() + " does not have " + Constants.PERMISSION_USE);
      return;
    }

    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> SwapUtil.performSwap(player, itemType));
  }
}
