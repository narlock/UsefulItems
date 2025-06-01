package com.narlock.usefulitems.listeners;

import com.narlock.usefulitems.UsefulItems;
import com.narlock.usefulitems.util.ProtectionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;

public class EnhancedItemDamageListener extends EntityListener {
    private final ProtectionManager protectionManager;

    public EnhancedItemDamageListener(UsefulItems plugin) {
        this.protectionManager = plugin.getProtectionManager();
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        String playerName = player.getName();

        // Check god mode
        if (protectionManager.isGodMode(playerName)) {
            event.setCancelled(true);
            return;
        }

        // Check suffocation/drowning protection
        if (protectionManager.hasSuffocationProtection(playerName) &&
                (event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION ||
                        event.getCause() == EntityDamageEvent.DamageCause.DROWNING)) {
            event.setCancelled(true);
            return;
        }

        // Check fall damage protection
        if (protectionManager.hasFallDamageProtection(playerName) &&
                (event.getCause() == EntityDamageEvent.DamageCause.FALL)) {
            event.setCancelled(true);
            return;
        }

        // Check chainmail fire protection
        if (protectionManager.hasFullChainmail(player) &&
                (event.getCause() == EntityDamageEvent.DamageCause.FIRE ||
                        event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK ||
                        event.getCause() == EntityDamageEvent.DamageCause.LAVA)) {
            event.setCancelled(true);
        }
    }
}
