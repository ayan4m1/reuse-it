package in.thekreml.reuseit.listener;

import in.thekreml.reuseit.ReuseIt;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class PlayerStackListener implements Listener {
  private ReuseIt plugin;

  public PlayerStackListener(ReuseIt plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerUse(PlayerInteractEvent playerInteractEvent) {
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

    if (!plugin.getConfigModel().getMaterials().contains(stack.getData().getItemType())) {
      return;
    }

    if (stack.getAmount() != 1) {
      return;
    }

    final Player player = playerInteractEvent.getPlayer();
    final ItemStack[] storage = player.getInventory().getContents();

    for (int i = 0; i < storage.length; i++) {
      final ItemStack srcSlot = storage[i];
      if (srcSlot.getData() == null || srcSlot.getData().getItemType() != stack.getData().getItemType()) {
        continue;
      }

      player.getInventory().setItemInMainHand(srcSlot);
      player.getInventory().clear(i);
    }
  }
}
