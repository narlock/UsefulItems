package com.narlock.usefulitems.util;

import com.narlock.usefulitems.UsefulItems;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Tracks temporary protections
 *
 * @author narlock
 */
public class ProtectionManager {
    private final UsefulItems plugin;

    Set<String> godModePlayers = new HashSet<>();
    Map<String, Long> suffocationProtectionEnd = new HashMap<>(); // username, time remaining in ms
    Map<String, Long> fallDamageProtectionEnd = new HashMap<>(); // username, time remaining in ms

    public ProtectionManager(UsefulItems plugin) {
        this.plugin = plugin;
    }

    public boolean isGodMode(String playerName) {
        return godModePlayers.contains(playerName);
    }

    /**
     * God mode is given to a player for 5 seconds. It cannot be stacked, so
     * the server scheduler system is used for handling when to disable God mode.
     *
     * @param playerName the player's username
     * @param durationSeconds how long to enable god mode
     */
    public void enableGodMode(String playerName, int durationSeconds) {
        godModePlayers.add(playerName);

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                godModePlayers.remove(playerName);
                Player player = plugin.getServer().getPlayer(playerName);
                if (player != null) {
                    player.sendMessage("§cYour god mode has expired.");
                }
            }
        }, durationSeconds * 20L); // 20 ticks = 1 second
    }

    /**
     * When suffocation protection is given to a player, they have the ability
     * to stack it. This approach uses the system time to compare against the
     * time that they have from suffocationProtectionEnd map.
     *
     * @param playerName the player's name
     * @param additionalSeconds seconds to add to suffocationProtectionEnd map
     */
    public void addSuffocationProtection(String playerName, int additionalSeconds) {
        long currentEnd = suffocationProtectionEnd.getOrDefault(playerName, System.currentTimeMillis());
        long newEnd = Math.max(currentEnd, System.currentTimeMillis()) + additionalSeconds * 1000L;
        suffocationProtectionEnd.put(playerName, newEnd);
    }

    public boolean hasSuffocationProtection(String playerName) {
        Long endTime = suffocationProtectionEnd.get(playerName);
        return endTime != null && System.currentTimeMillis() < endTime;
    }

    public long getRemainingSuffocationTime(String playerName) {
        long end = suffocationProtectionEnd.getOrDefault(playerName, 0L);
        long remaining = end - System.currentTimeMillis();
        return Math.max(0, remaining);
    }

    public void addFallDamageProtection(String playerName, int additionalSeconds) {
        long currentEnd = fallDamageProtectionEnd.getOrDefault(playerName, System.currentTimeMillis());
        long newEnd = Math.max(currentEnd, System.currentTimeMillis()) + additionalSeconds * 1000L;
        fallDamageProtectionEnd.put(playerName, newEnd);
    }

    public boolean hasFallDamageProtection(String playerName) {
        Long endTime = fallDamageProtectionEnd.get(playerName);
        return endTime != null && System.currentTimeMillis() < endTime;
    }

    public long getRemainingFallDamageProtectionTime(String playerName) {
        long end = fallDamageProtectionEnd.getOrDefault(playerName, 0L);
        long remaining = end - System.currentTimeMillis();
        return Math.max(0, remaining);
    }

    public boolean hasFullChainmail(Player player) {
        ItemStack[] armor = player.getInventory().getArmorContents();
        if (armor == null || armor.length < 4) {
            return false;
        }
        return armor[0].getType() == Material.CHAINMAIL_BOOTS &&
                armor[1].getType() == Material.CHAINMAIL_LEGGINGS &&
                armor[2].getType() == Material.CHAINMAIL_CHESTPLATE &&
                armor[3].getType() == Material.CHAINMAIL_HELMET;
    }
}
