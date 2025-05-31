package com.narlock.usefulitems.listeners;

import com.narlock.usefulitems.UsefulItems;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.narlock.usefulitems.util.Utils.*;
import static com.narlock.usefulitems.util.Utils.GoldToolType.GOLD_PICKAXE;

/**
 * Handles enhanced block breaking
 *
 * @author narlock
 */
public class EnhancedBlockListener extends BlockListener {
    private final UsefulItems plugin;
    private final Map<Integer, List<Integer>> enhancedBlockRules = new HashMap<>();

    public EnhancedBlockListener(UsefulItems plugin) {
        this.plugin = plugin;
        initializeEnhancedBlockRules();
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        Block block = event.getBlock();
        ItemStack heldItem = player.getItemInHand();

        // Exit out if no held item
        if (heldItem == null) {
            return;
        }

        int blockId = block.getTypeId();

        int heldItemId = heldItem.getTypeId();
        List<Integer> tools = enhancedBlockRules.get(blockId);
        if (isEnhancedBlockBreakDisabled(blockId, heldItemId, tools)) {
            return;
        }

        // Apply custom break logic
        event.setCancelled(true);
        block.setTypeId(0); // Remove the block

        // Drop the item
        ItemStack drop = new ItemStack(blockId, 1, block.getData());
        block.getWorld().dropItemNaturally(block.getLocation(), drop);

        // Damage or break tool if a tool was used
        if (isTool(heldItem)) {
            applyToolDurability(player, heldItem);
        }
    }

    public void initializeEnhancedBlockRules() {
        enhancedBlockRules.put(67, PICKAXE_TOOL_IDS); // Cobble stairs
    }

    public boolean isEnhancedBlockBreakDisabled(int blockId, int heldItemId, List<Integer> tools) {
        switch (blockId) {
            case 53:
                // Wood Stairs
                return !plugin.getConfigManager().isStairsDropStairsEnabled();
            case 67:
                // Cobble Stairs
                if (tools == null || !tools.contains(heldItemId)) {
                    return true;
                }

                return !plugin.getConfigManager().isStairsDropStairsEnabled();
            default:
                return true;
        }
    }
}
