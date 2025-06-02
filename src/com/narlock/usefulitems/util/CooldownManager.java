package com.narlock.usefulitems.util;

import com.narlock.usefulitems.UsefulItems;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Manages cooldowns (e.g., fireball)
 *
 * @author narlock
 */
public class CooldownManager {
    private final UsefulItems plugin;

    Set<String> fireballCooldownPlayers = new HashSet<>();

    public CooldownManager(UsefulItems plugin) {
        this.plugin = plugin;
    }

    public boolean isInFireballCooldown(String playerName) {
        return fireballCooldownPlayers.contains(playerName);
    }

    /**
     * Fireball cooldown is given to a player for {durationSeconds} defined in the config.
     *
     * @param playerName the player's username
     * @param durationSeconds how long the cooldown will last
     */
    public void enableFireballCooldown(String playerName, int durationSeconds) {
        fireballCooldownPlayers.add(playerName);

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
           fireballCooldownPlayers.remove(playerName);
           Player player = plugin.getServer().getPlayer(playerName);
           if (player != null) {
               player.sendMessage("§aFireball cooldown has expired!");
           }
        }, durationSeconds * 20L);
    }
}
