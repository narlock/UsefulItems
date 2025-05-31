package com.narlock.usefulitems.commands;

import com.narlock.usefulitems.UsefulItems;
import com.narlock.usefulitems.config.ConfigManager;
import com.narlock.usefulitems.util.PermissionsUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Handles /craft command
 *
 * @author narlock
 */
public class CraftCommand implements CommandExecutor {
    private final ConfigManager configManager;

    public CraftCommand(UsefulItems plugin) {
        this.configManager = plugin.getConfigManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!PermissionsUtil.canExtendedCraft(player)) {
            player.sendMessage("§cYou don't have permission to use extended crafting!");
            return true;
        }

        if (args.length < 1 || args.length > 2) {
            sendUsageMessage(player);
            return true;
        }

        String item = args[0].toLowerCase();
        if("help".equals(item)) {
            player.sendMessage("§bExtended Crafting");
            if(configManager.isExtendedCraftingEnabled("cobweb")) {
                player.sendMessage("§cobweb: 9 string.");
            }
            if(configManager.isExtendedCraftingEnabled("sponge")) {
                player.sendMessage("§sponge: 1 wool, 1 yellow flower, 2 clay blocks, 1 water bucket, 4 slimeballs.");
            }
            if(configManager.isExtendedCraftingEnabled("mossycobble")) {
                player.sendMessage("§amossycobble (4): 4 cobble, 2 leaf blocks, 2 slimeballs.");
            }
            if(configManager.isExtendedCraftingEnabled("doublestoneslab")) {
                player.sendMessage("§adoublestoneslab: 2 stone slabs, 1 clay ball.");
            }
            if(configManager.isExtendedCraftingEnabled("fire")) {
                player.sendMessage("§afire: 3 gunpowder, 3 netherrack, 2 glowstone dust, 1 flint.");
            }
            return true;
        }

        int amount = 1;
        if(args.length == 2) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sendUsageMessage(player);
                return true;
            }
        }

        switch (item) {
            case "cobweb":
                craftCobweb(player, amount);
                break;
            case "sponge":
                craftSponge(player, amount);
                break;
            case "mossycobble":
            case "mossycobblestone":
                craftMossyCobble(player, amount);
                break;
            case "doublestoneslab":
            case "doublestep":
                craftDoubleStoneSlab(player, amount);
                break;
            case "fire":
                craftFire(player, amount);
                break;
            default:
                player.sendMessage("§cUnknown craftable item: " + item);
        }

        return true;
    }

    private void sendUsageMessage(Player player) {
        player.sendMessage("§cUsage: /craft <item> <optional: amount>");
        player.sendMessage("§bView extended crafting: /craft help");
    }

    private void craftCobweb(Player player, int amount) {
        if (!configManager.isExtendedCraftingEnabled("cobweb")) {
            player.sendMessage("§cCobweb crafting is disabled.");
            return;
        }
        int stringCost = 9 * amount;
        if (countItem(player, Material.STRING) < stringCost) {
            player.sendMessage(String.format("§cYou need %d string to craft %s cobweb(s).", stringCost, amount));
            return;
        }
        removeItems(player, Material.STRING, stringCost);
        player.getInventory().addItem(new ItemStack(30, amount)); // Cobweb block
        player.sendMessage(String.format("§aYou crafted %d cobweb(s)!", amount));
    }

    private void craftSponge(Player player, int amount) {
        if (!configManager.isExtendedCraftingEnabled("sponge")) {
            player.sendMessage("§cSponge crafting is disabled.");
            return;
        }

        int woolCost = 1 * amount;
        int yellowFlowerCost = 1 * amount;
        int clayCost = 2 * amount;
        int waterBucketCost = 1 * amount;
        int slimeballCost = 4 * amount;

        if (countItem(player, Material.WOOL) < woolCost ||
                countItem(player, Material.YELLOW_FLOWER) < yellowFlowerCost ||
                countItem(player, Material.CLAY) < clayCost ||
                countItem(player, Material.WATER_BUCKET) < waterBucketCost ||
                countItem(player, Material.SLIME_BALL) < slimeballCost) {
            player.sendMessage(String.format("§cYou need %d wool block(s), %d yellow flower(s), %d clay blocks, %d water bucket(s), and %d slimeballs to craft %d sponge(s).",
                    woolCost, yellowFlowerCost, clayCost, waterBucketCost, slimeballCost, amount));
            return;
        }

        removeItems(player, Material.WOOL, woolCost);
        removeItems(player, Material.CLAY, clayCost);
        removeItems(player, Material.WATER_BUCKET, waterBucketCost);
        removeItems(player, Material.SLIME_BALL, slimeballCost);

        player.getInventory().addItem(new ItemStack(Material.SPONGE, amount));
        player.sendMessage(String.format("§aYou crafted %d sponge(s)!", amount));
    }

    private void craftMossyCobble(Player player, int amount) {
        if (!configManager.isExtendedCraftingEnabled("mossycobble")) {
            player.sendMessage("§cMossy Cobblestone crafting is disabled.");
            return;
        }

        int cobbleCost = 4 * amount;
        int leavesCost = 2 * amount;
        int slimeballCost = 2 * amount;

        if (countItem(player, Material.COBBLESTONE) < cobbleCost ||
                countItem(player, Material.LEAVES) < leavesCost ||
                countItem(player, Material.SLIME_BALL) < slimeballCost) {
            player.sendMessage(String.format("§cYou need %d cobblestone, %d leaves, and %d slimeballs to craft %d mossy cobblestone(s).",
                    cobbleCost, leavesCost, slimeballCost, 4 * amount));
            return;
        }

        removeItems(player, Material.COBBLESTONE, cobbleCost);
        removeItems(player, Material.LEAVES, leavesCost);
        removeItems(player, Material.SLIME_BALL, slimeballCost);

        player.getInventory().addItem(new ItemStack(Material.MOSSY_COBBLESTONE, 4 * amount));
        player.sendMessage(String.format("§aYou crafted %d mossy cobblestone(s)!", 4 * amount));
    }

    private void craftDoubleStoneSlab(Player player, int amount) {
        if (!configManager.isExtendedCraftingEnabled("doublestoneslab")) {
            player.sendMessage("§cDouble Stone Slab crafting is disabled.");
            return;
        }

        int slabCost = 2 * amount;
        int clayCost = 1 * amount;

        if (countItem(player, Material.STEP) < slabCost ||
                countItem(player, Material.CLAY_BALL) < clayCost) {
            player.sendMessage(String.format("§cYou need %d stone slabs and %d clay to craft %d double stone slab(s).",
                    slabCost, clayCost, amount));
            return;
        }

        removeItems(player, Material.STEP, slabCost);
        removeItems(player, Material.CLAY_BALL, clayCost);

        player.getInventory().addItem(new ItemStack(Material.DOUBLE_STEP, amount));
        player.sendMessage(String.format("§aYou crafted %d double stone slab(s)!", amount));
    }

    private void craftFire(Player player, int amount) {
        if (!configManager.isExtendedCraftingEnabled("fire")) {
            player.sendMessage("§cFire crafting is disabled.");
            return;
        }

        int gunpowderCost = 3 * amount;
        int netherrackCost = 3 * amount;
        int glowstoneDustCost = 2 * amount;
        int flintCost = 1 * amount;

        if (countItem(player, Material.SULPHUR) < gunpowderCost ||
                countItem(player, Material.NETHERRACK) < netherrackCost ||
                countItem(player, Material.GLOWSTONE_DUST) < glowstoneDustCost ||
                countItem(player, Material.FLINT) < flintCost) {
            player.sendMessage(String.format("§cYou need %d gunpowder, %d netherrack, %d glowstone dust, and %d flint to craft %d fire block(s).",
                    gunpowderCost, netherrackCost, glowstoneDustCost, flintCost, amount));
            return;
        }

        removeItems(player, Material.SULPHUR, gunpowderCost);
        removeItems(player, Material.NETHERRACK, netherrackCost);
        removeItems(player, Material.GLOWSTONE_DUST, glowstoneDustCost);
        removeItems(player, Material.FLINT, flintCost);

        player.getInventory().addItem(new ItemStack(Material.FIRE, amount));
        player.sendMessage(String.format("§aYou crafted %d fire block(s)!", amount));
    }

    /**
     * Counts the amount of {material} in {player} inventory
     * @param player executing the command
     * @param material to count
     * @return amount of {material}
     */
    private int countItem(Player player, Material material) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == material) {
                count += item.getAmount();
            }
        }
        return count;
    }

    /**
     * Removes {amount} of {material} item in {player} inventory.
     * @param player executing the command
     * @param material to remove
     * @param amount to remove
     */
    private void removeItems(Player player, Material material, int amount) {
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null && item.getType() == material) {
                int itemAmount = item.getAmount();
                if (itemAmount <= amount) {
                    amount -= itemAmount;
                    contents[i] = null;
                } else {
                    item.setAmount(itemAmount - amount);
                    break;
                }
            }
        }
        player.getInventory().setContents(contents);
    }
}
