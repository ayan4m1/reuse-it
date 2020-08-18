package in.thekreml.reuseit.listener;

import in.thekreml.reuseit.Constants;
import in.thekreml.reuseit.ReuseIt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerStackListener implements Listener {
  private final ReuseIt plugin;

  public PlayerStackListener(ReuseIt plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
    if (playerInteractEvent.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }

    final Block block = playerInteractEvent.getClickedBlock();
    if (block == null) {
      return;
    }

    if (block.getBlockData().getMaterial() != Material.FARMLAND) {
      return;
    }

    if (!playerInteractEvent.hasItem()) {
      return;
    }

    final ItemStack stack = playerInteractEvent.getItem();
    if (stack == null || stack.getData() == null) {
      return;
    }

    if (stack.getAmount() != 1) {
      return;
    }

    final String itemType = stack.getData().getItemType().name();

    if (!plugin.getConfigModel().getMaterials().contains(stack.getData().getItemType())) {
      plugin.getLog().fine(itemType + " is not eligible for reuse!");
      return;
    }

    final Player player = playerInteractEvent.getPlayer();

    if (!plugin.getPermissions().has(player, Constants.PERMISSION_USE)) {
      plugin.getLog().info(player.getName() + " does not have " + Constants.PERMISSION_USE);
    }

    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
      plugin.getLog().fine("Searching for a stack of " + itemType);
      final ItemStack[] storage = player.getInventory().getStorageContents();

      for (int i = 0; i < storage.length; i++) {
        if (i == player.getInventory().getHeldItemSlot()) {
          continue;
        }

        final ItemStack srcSlot = storage[i];
        if (srcSlot == null || srcSlot.getData() == null || !srcSlot.getData().getItemType().name().equals(itemType)) {
          continue;
        }

        plugin.getLog().fine("Swapping slots for " + player.getName());
        player.getInventory().setItemInMainHand(srcSlot);
        player.getInventory().clear(i);
        player.updateInventory();
        break;
      }
    });
  }
}
