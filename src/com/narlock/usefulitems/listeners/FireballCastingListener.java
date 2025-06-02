package com.narlock.usefulitems.listeners;

import com.narlock.usefulitems.UsefulItems;
import com.narlock.usefulitems.util.CooldownManager;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class FireballCastingListener extends PlayerListener {
    private final UsefulItems plugin;
    private final CooldownManager cooldownManager;

    public FireballCastingListener(UsefulItems plugin) {
        this.plugin = plugin;
        this.cooldownManager = plugin.getCooldownManager();
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack heldItem = player.getItemInHand();

        if (heldItem != null && heldItem.getType() == Material.GOLD_SWORD) {
            switch (event.getAction()) {
                case RIGHT_CLICK_AIR:
                case RIGHT_CLICK_BLOCK:
                    // Check cooldown
                    if(cooldownManager.isInFireballCooldown(player.getName())) {
                       player.sendMessage("§cYou must wait before using another Fireball!");
                       break;
                    }

                    if (consumeFireAmmo(player)) {
                        // Set fireball cooldown
                        int duration = plugin.getConfigManager()
                                .getEnhancedItemSetting("fireball", "cooldown", 5);
                        cooldownManager.enableFireballCooldown(player.getName(), duration);

                        // Spawn Fireball
                        spawnFireball(player);
                    } else {
                        player.sendMessage("§cYou need at least one fire block to cast a fireball!");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private boolean consumeFireAmmo(Player player) {
        ItemStack[] contents = player.getInventory().getContents();

        // Search for fire in player's inventory
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null && item.getType() == Material.FIRE) {
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                } else {
                    contents[i] = null;
                }
                player.getInventory().setContents(contents);
                player.updateInventory();
                return true;
            }
        }

        return false;
    }

    private void spawnFireball(Player player) {
        Vector direction = player.getEyeLocation().getDirection().multiply(2);
        player.getWorld().spawn(player.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), Fireball.class);
    }
}
