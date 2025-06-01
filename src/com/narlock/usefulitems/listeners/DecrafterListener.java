package com.narlock.usefulitems.listeners;

import com.narlock.usefulitems.UsefulItems;
import com.narlock.usefulitems.config.BookshelfNoteManager;
import com.narlock.usefulitems.util.PermissionsUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static com.narlock.usefulitems.util.Utils.*;

/**
 * Handles gold tool decrafting
 *
 * @author narlock
 */
public class DecrafterListener extends BlockListener {
    private final UsefulItems plugin;
    private final Map<Integer, DecraftRule> decraftRules = new HashMap<>();

    public DecrafterListener(UsefulItems plugin) {
        this.plugin = plugin;
        initializeDecraftRules();
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        // Exit if player does not have permission
        if (!PermissionsUtil.canDecraft(player)) {
            return;
        }

        Block block = event.getBlock();
        ItemStack heldItem = player.getItemInHand();

        // Exit out if no held item
        if (heldItem == null) {
            return;
        }

        int blockId = block.getTypeId();
        int heldItemId = heldItem.getTypeId();
        DecraftRule rule = decraftRules.get(blockId);

        // Exit if no rule for block, not using gold tool, or block is disabled in config
        if (rule == null
                || !GoldToolType.matches(rule.getRequiredTool(), heldItemId)
                || !plugin.getConfigManager().isDecrafterBlockEnabled(blockId)) {
            return;
        }

        // Apply decrafting logic
        event.setCancelled(true);

        // Removes the block
        block.setTypeId(0);

        // Drop items
        for (ItemStack drop : rule.getDrops()) {
            block.getWorld().dropItemNaturally(block.getLocation(), drop);
        }

        // Damage or break tool
        applyToolDurability(player, heldItem);
    }

    /**
     * Represents a decrafting rule that defines the gold tool and items that drop when the block is decrafted.
     * <p>
     * Example:
     * requiredTool: GOLD_AXE
     * drops: [ Material.WOOD, 4 ]
     * This can be paired inside a hashmap with 58 (Workbench).
     * Used inside DecrafterListener to control block-specific decrafting behavior.
     */
    public class DecraftRule {
        private final GoldToolType requiredTool;
        private final ItemStack[] drops;

        public DecraftRule(GoldToolType requiredTool, ItemStack... drops) {
            this.requiredTool = requiredTool;
            this.drops = drops;
        }

        public GoldToolType getRequiredTool() {
            return requiredTool;
        }

        public ItemStack[] getDrops() {
            return drops;
        }
    }

    public void initializeDecraftRules() {
        decraftRules.put(23, new DecraftRule(GoldToolType.GOLD_PICKAXE,
                new ItemStack(Material.COBBLESTONE, 7),
                new ItemStack(Material.BOW, 1),
                new ItemStack(Material.REDSTONE, 1))); // Dispenser
        decraftRules.put(24, new DecraftRule(GoldToolType.GOLD_PICKAXE,
                new ItemStack(Material.SAND, 4))); // Sandstone
        decraftRules.put(25, new DecraftRule(GoldToolType.GOLD_AXE,
                new ItemStack(Material.WOOD, 8),
                new ItemStack(Material.REDSTONE, 1))); // Noteblock
        decraftRules.put(26, new DecraftRule(GoldToolType.GOLD_AXE,
                new ItemStack(Material.WOOL, 3),
                new ItemStack(Material.WOOD, 3))); // Bed
        decraftRules.put(29, new DecraftRule(GoldToolType.GOLD_AXE,
                new ItemStack(Material.PISTON_BASE, 1),
                new ItemStack(Material.SLIME_BALL, 1))); // Sticky Piston
        decraftRules.put(33, new DecraftRule(GoldToolType.GOLD_AXE,
                new ItemStack(Material.WOOD, 3),
                new ItemStack(Material.COBBLESTONE, 4),
                new ItemStack(Material.IRON_INGOT, 1),
                new ItemStack(Material.REDSTONE, 1))); // Piston
        decraftRules.put(58, new DecraftRule(GoldToolType.GOLD_AXE,
                new ItemStack(Material.WOOD, 4))); // Workbench
        decraftRules.put(61, new DecraftRule(GoldToolType.GOLD_PICKAXE,
                new ItemStack(Material.COBBLESTONE, 8))); // Furnace
        decraftRules.put(76, new DecraftRule(GoldToolType.GOLD_AXE,
                new ItemStack(Material.REDSTONE, 1),
                new ItemStack(Material.STICK, 1))); // Redstone Torch
        decraftRules.put(64, new DecraftRule(GoldToolType.GOLD_AXE,
                new ItemStack(Material.WOOD, 6))); // Wooden Door
        decraftRules.put(69, new DecraftRule(GoldToolType.GOLD_AXE,
                new ItemStack(Material.STICK, 1),
                new ItemStack(Material.COBBLESTONE, 1))); // Lever
        decraftRules.put(70, new DecraftRule(GoldToolType.GOLD_PICKAXE,
                new ItemStack(Material.STONE, 2))); // Stone Pressure Plate
        decraftRules.put(71, new DecraftRule(GoldToolType.GOLD_PICKAXE,
                new ItemStack(Material.IRON_INGOT, 6))); // Iron Door
        decraftRules.put(72, new DecraftRule(GoldToolType.GOLD_AXE,
                new ItemStack(Material.WOOD, 2))); // Wooden Pressure Plate
        decraftRules.put(77, new DecraftRule(GoldToolType.GOLD_PICKAXE,
                new ItemStack(Material.STONE, 2))); // Stone Button
        decraftRules.put(84, new DecraftRule(GoldToolType.GOLD_AXE,
                new ItemStack(Material.WOOD, 8),
                new ItemStack(Material.DIAMOND, 1))); // Jukebox
        decraftRules.put(91, new DecraftRule(GoldToolType.GOLD_AXE,
                new ItemStack(Material.PUMPKIN, 1),
                new ItemStack(Material.TORCH, 1))); // Jack 'o' Lantern
        decraftRules.put(96, new DecraftRule(GoldToolType.GOLD_AXE,
                new ItemStack(Material.WOOD, 3))); // Trapdoor
        decraftRules.put(85, new DecraftRule(GoldToolType.GOLD_AXE,
                new ItemStack(Material.STICK, 3))); // Fence
        decraftRules.put(63, new DecraftRule(GoldToolType.GOLD_AXE,
                new ItemStack(Material.WOOD, 6),
                new ItemStack(Material.STICK, 1))); // Sign Post
        decraftRules.put(68, new DecraftRule(GoldToolType.GOLD_AXE,
                new ItemStack(Material.WOOD, 6),
                new ItemStack(Material.STICK, 1))); // Wall Sign
        decraftRules.put(93, new DecraftRule(GoldToolType.GOLD_PICKAXE,
                new ItemStack(Material.STONE, 3),
                new ItemStack(Material.REDSTONE_TORCH_ON, 2),
                new ItemStack(Material.REDSTONE, 1))); // Redstone Repeater (off)
        decraftRules.put(94, new DecraftRule(GoldToolType.GOLD_PICKAXE,
                new ItemStack(Material.STONE, 3),
                new ItemStack(Material.REDSTONE_TORCH_ON, 2),
                new ItemStack(Material.REDSTONE, 1))); // Redstone Repeater (on)
        decraftRules.put(45, new DecraftRule(GoldToolType.GOLD_PICKAXE,
                new ItemStack(Material.CLAY_BRICK, 4))); // Brick Block
        decraftRules.put(41, new DecraftRule(GoldToolType.GOLD_PICKAXE,
                new ItemStack(Material.GOLD_INGOT, 9))); // Gold Block
        decraftRules.put(42, new DecraftRule(GoldToolType.GOLD_PICKAXE,
                new ItemStack(Material.IRON_INGOT, 9))); // Iron Block
        decraftRules.put(57, new DecraftRule(GoldToolType.GOLD_PICKAXE,
                new ItemStack(Material.DIAMOND, 9))); // Diamond Block
    }
}
