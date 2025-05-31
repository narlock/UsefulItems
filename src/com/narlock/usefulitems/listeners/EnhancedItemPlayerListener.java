package com.narlock.usefulitems.listeners;

import com.narlock.usefulitems.UsefulItems;
import com.narlock.usefulitems.util.ProtectionManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;

/**
 * Handles sugar, milk, fireball uses
 *
 * @author narlock
 */
public class EnhancedItemPlayerListener extends PlayerListener {
    private final UsefulItems plugin;
    private final ProtectionManager protectionManager;

    public EnhancedItemPlayerListener(UsefulItems plugin) {
        this.plugin = plugin;
        this.protectionManager = plugin.getProtectionManager();
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack heldItem = player.getItemInHand();

        if (heldItem == null) return;

        Material type = heldItem.getType();

        // Milk Bucket → God Mode
        if (type == Material.MILK_BUCKET) {
            if (!plugin.getConfigManager().isFeatureEnabled("enhanceditems")) return;

            int duration = plugin.getConfigManager().getEnhancedItemSetting("milkbucket", "godmode-duration", 5);

            protectionManager.enableGodMode(player.getName(), duration);
            player.sendMessage("§aYou are now in God mode for " + duration + " seconds!");

            // Consume the milk bucket (replaced with empty bucket)
            heldItem.setType(Material.BUCKET);
            event.setCancelled(true);
        }

        // Sugar → Suffocation Protection
        if (type == Material.SUGAR) {
            if (!plugin.getConfigManager().isFeatureEnabled("enhanceditems")) return;

            int duration = plugin.getConfigManager().getEnhancedItemSetting("sugar", "suffocation-duration", 3);

            protectionManager.addSuffocationProtection(player.getName(), duration);
            long remaining = protectionManager.getRemainingSuffocationTime(player.getName());

            player.sendMessage("§aYou gained suffocation protection! Time remaining: " + (remaining / 1000) + " seconds.");

            // Consume 1 sugar
            int amount = heldItem.getAmount();
            if (amount <= 1) {
                player.setItemInHand(null);
            } else {
                heldItem.setAmount(amount - 1);
            }
            event.setCancelled(true);
        }
    }
}
