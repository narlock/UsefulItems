package com.narlock.usefulitems.listeners;

import com.narlock.usefulitems.UsefulItems;
import com.narlock.usefulitems.util.PermissionsUtil;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static com.narlock.usefulitems.util.Utils.GoldToolType;
import static com.narlock.usefulitems.util.Utils.applyToolDurability;

/**
 * Handles gold tool silk touch drops
 *
 * @author narlock
 */
public class SilkTouchListener extends BlockListener {
    private final UsefulItems plugin;
    private final Map<Integer, GoldToolType> silkTouchRules = new HashMap<>();

    public SilkTouchListener(UsefulItems plugin) {
        this.plugin = plugin;
        initializeSilkTouchRules();
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        // Exit if player does not have permission
        if (!PermissionsUtil.canSilkTouch(player)) {
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
        GoldToolType goldToolType = silkTouchRules.get(blockId);

        // Exit if no gold tool enabled silk touch for block, or block is disabled in config
        if (goldToolType == null
                || !GoldToolType.matches(goldToolType, heldItemId)
                || !plugin.getConfigManager().isSilkTouchBlockEnabled(blockId)) {
            return;
        }

        // Apply silk touch logic
        event.setCancelled(true);
        block.setTypeId(0); // Remove the block

        // Drop the item
        ItemStack silkDrop = new ItemStack(blockId, 1, block.getData());
        block.getWorld().dropItemNaturally(block.getLocation(), silkDrop);

        // Damage or break tool
        applyToolDurability(player, heldItem);
    }

    public void initializeSilkTouchRules() {
        silkTouchRules.put(1, GoldToolType.GOLD_PICKAXE); // Stone
        silkTouchRules.put(16, GoldToolType.GOLD_PICKAXE); // Coal Ore
        silkTouchRules.put(20, GoldToolType.GOLD_PICKAXE); // Glass
        silkTouchRules.put(21, GoldToolType.GOLD_PICKAXE); // Lapis Ore
        silkTouchRules.put(30, GoldToolType.GOLD_SWORD); // Cobweb
        silkTouchRules.put(31, GoldToolType.GOLD_SWORD); // Fern
        silkTouchRules.put(47, GoldToolType.GOLD_AXE); // Bookshelf
        silkTouchRules.put(60, GoldToolType.GOLD_HOE); // Tilled Dirt
        silkTouchRules.put(56, GoldToolType.GOLD_PICKAXE); // Diamond Ore
        silkTouchRules.put(73, GoldToolType.GOLD_PICKAXE); // Redstone Ore
        silkTouchRules.put(78, GoldToolType.GOLD_SHOVEL); // Snow Layer
        silkTouchRules.put(79, GoldToolType.GOLD_PICKAXE); // Ice
        silkTouchRules.put(80, GoldToolType.GOLD_SHOVEL); // Snow Block
        silkTouchRules.put(89, GoldToolType.GOLD_PICKAXE); // Glowstone
    }
}
