package in.thekreml.reuseit.listener;

import in.thekreml.reuseit.ReuseIt;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FoodLevelChangeListener implements Listener {
  private static final Integer FOOD_LEVEL_FULL = 20;
  private static final Integer FOOD_LEVEL_MIN_REGEN = 19;
  private static final Map<Integer, Set<Material>> FOODS = Stream.of(
      new AbstractMap.SimpleEntry<>(1, Collections.singleton(Material.DRIED_KELP)),
      new AbstractMap.SimpleEntry<>(2, new HashSet<>(Arrays.asList(Material.COOKIE, Material.MELON_SLICE, Material.SWEET_BERRIES))),
      new AbstractMap.SimpleEntry<>(4, Collections.singleton(Material.APPLE)),
      new AbstractMap.SimpleEntry<>(5, new HashSet<>(Arrays.asList(Material.BAKED_POTATO, Material.BREAD, Material.COOKED_COD, Material.COOKED_RABBIT))),
      new AbstractMap.SimpleEntry<>(6, new HashSet<>(Arrays.asList(Material.BEETROOT_SOUP, Material.COOKED_CHICKEN, Material.COOKED_MUTTON, Material.COOKED_SALMON, Material.MUSHROOM_STEW))),
      new AbstractMap.SimpleEntry<>(8, new HashSet<>(Arrays.asList(Material.COOKED_PORKCHOP, Material.PUMPKIN_PIE, Material.COOKED_BEEF))),
      new AbstractMap.SimpleEntry<>(10, Collections.singleton(Material.RABBIT_STEW))
  ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> newValue, TreeMap::new));

  private final ReuseIt plugin;

  public FoodLevelChangeListener(ReuseIt plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onFoodLevelChange(FoodLevelChangeEvent event) {
    if (!plugin.getConfigModel().isConsumeEnabled()) {
      return;
    }

    if (!(event.getEntity() instanceof final Player player)) {
      return;
    }

    final Inventory inventory = player.getInventory();
    final int remainingFood = event.getFoodLevel();

    plugin.getLog().info(String.join("", "Player ", player.getName(), " has ", String.valueOf(remainingFood), " food remaining"));

    if (remainingFood > FOOD_LEVEL_MIN_REGEN) {
      return;
    }

    for (final Map.Entry<Integer, Set<Material>> entry : FOODS.entrySet()) {
      final int foodGiven = entry.getKey();

      if (remainingFood > FOOD_LEVEL_FULL - foodGiven) {
        plugin.getLog().info(String.join("", "Stopping at required ", String.valueOf(foodGiven)));
        return;
      }

      final Set<Material> foods = entry.getValue();
      plugin.getLog().info(String.join("", "Considering ", String.valueOf(foods.size()), " foods"));

      final Optional<ItemStack> consumableFood = Arrays.stream(inventory.getContents())
          .filter((stack) -> (stack != null) &&
              foods.contains(stack.getType()) &&
              !stack.getItemMeta().hasLore() && !stack.getItemMeta().hasDisplayName())
          .findFirst();

      if (consumableFood.isPresent()) {
        final ItemStack foodStack = consumableFood.get();
        foodStack.setAmount(foodStack.getAmount() - 1);
        player.setFoodLevel(remainingFood + foodGiven);
        player.playSound(Sound.sound(Key.key("minecraft:entity_player_burp"), Sound.Source.PLAYER, 1.0f, 1.0f));
        event.setCancelled(true);
        return;
      }
    }
  }
}
