package in.thekreml.reuseit.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SwapUtil {
  public static void performSwap(Player player, String itemType) {
    final ItemStack[] storage = player.getInventory().getStorageContents();

    for (int i = 0; i < storage.length; i++) {
      if (i == player.getInventory().getHeldItemSlot()) {
        continue;
      }

      final ItemStack srcSlot = storage[i];
      if (srcSlot == null || srcSlot.getData() == null || !srcSlot.getData().getItemType().name().equals(itemType)) {
        continue;
      }

      player.getInventory().setItemInMainHand(srcSlot);
      player.getInventory().clear(i);
      player.updateInventory();
      break;
    }
  }
}
