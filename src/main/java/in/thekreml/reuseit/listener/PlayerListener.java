package in.thekreml.reuseit.listener;

import in.thekreml.reuseit.Constants;
import in.thekreml.reuseit.ReuseIt;
import in.thekreml.reuseit.utils.SwapUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
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

public class PlayerListener implements Listener {
  private final ReuseIt plugin;

  public PlayerListener(ReuseIt plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    final Player player = event.getPlayer();

    if (!plugin.getConfigModel().isInteractEnabled()) {
      return;
    }

    if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }

    final Block block = event.getClickedBlock();
    if (block == null) {
      return;
    }

    final ItemStack item = event.getItem();

    if (item == null || item.getData() == null) {
      return;
    }

    final String itemType = item.getData().getItemType().name();

    if (!plugin.getConfigModel().getInteractMaterials().contains(item.getData().getItemType())) {
      plugin.getLog().fine(itemType + " is not eligible for interact reuse!");
      return;
    }

    checkStack(player, item);
  }

  @EventHandler
  public void onPlayerBreakItem(PlayerItemBreakEvent event) {
    if (!plugin.getConfigModel().isBreakEnabled()) {
      return;
    }

    final ItemStack item = event.getBrokenItem();

    if (item.getData() == null) {
      return;
    }

    final String itemType = item.getData().getItemType().name();

    if (!plugin.getConfigModel().getBreakMaterials().contains(item.getData().getItemType())) {
      plugin.getLog().fine(itemType + " is not eligible for break reuse!");
      return;
    }

    checkStack(event.getPlayer(), item);
  }

  @EventHandler
  public void onPlayerConsume(PlayerItemConsumeEvent event) {
    if (!plugin.getConfigModel().isConsumeEnabled()) {
      return;
    }

    final ItemStack item = event.getItem();

    if (item.getData() == null) {
      return;
    }

    final String itemType = item.getData().getItemType().name();

    if (!plugin.getConfigModel().getConsumeMaterials().contains(item.getData().getItemType())) {
      plugin.getLog().fine(itemType + " is not eligible for consume reuse!");
      return;
    }

    checkStack(event.getPlayer(), item);
  }

  @EventHandler
  public void onProjectileLaunch(ProjectileLaunchEvent event) {
    if (!plugin.getConfigModel().isThrowEnabled()) {
      return;
    }

    final Projectile entity = event.getEntity();

    if (!(entity.getShooter() instanceof Player)) {
      return;
    }

    final Player player = (Player)entity.getShooter();
    final ItemStack stack = player.getInventory().getItemInMainHand();

    if (stack.getData() == null) {
      return;
    }

    final String itemType = stack.getData().getItemType().name();

    if (!plugin.getConfigModel().getThrowMaterials().contains(stack.getData().getItemType())) {
      plugin.getLog().fine(itemType + " is not eligible for consume reuse!");
      return;
    }

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

    if (!plugin.getPermissions().has(player, Constants.PERMISSION_USE)) {
      plugin.getLog().info(player.getName() + " does not have " + Constants.PERMISSION_USE);
      return;
    }

    final String itemType = item.getData().getItemType().name();

    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> SwapUtil.performSwap(player, itemType));
  }
}
