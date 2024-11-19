# reuse-it

## prerequisites

 * [Paper](https://papermc.io/downloads) 1.19 - 1.21
 * [Vault](https://github.com/MilkBowl/VaultAPI) 1.7.1
 * A plugin that supports the Vault Permissions API

## usage

When a player breaks an item, consumes food, places an item, or throws the last item of a stack, this plugin
will search for another stack of the same item in the player's inventory and move it to the held item slot if found.

When a player connects to the server, a message notifies them whether reuse-it is enabled or not.

Players can use the `/reuse` command to toggle the stack replacement functionality on and off.


## configuration

The following configuration options are available in `plugins/reuse-it/config.yml`:

 * **interact.enabled** - If true, then stacks will be replaced when a player interacts (i.e. right-clicks while holding) an item.
 * **interact.materials** - A list of materials that can be replaced when placed.
 * **throw.enabled** - If true, then stacks will be replaced when a player throws an item (i.e. potion or XP bottle)
 * **throw.materials** - A list of materials that can be replaced when thrown.
 * **break.enabled** - If true, then tools will be replaced when they break.
 * **break.materials** - A list of materials that can be replaced when broken (note: only materials with durability work!)
 * **consume.enabled** - If true, then foods/consumables will be replaced when a player consumes an item.
 * **consume.materials** - A list of materials that can be replaced when consumed.

## roadmap

 * tbd
