package com.narlock.usefulitems.listeners;

import com.narlock.usefulitems.UsefulItems;
import com.narlock.usefulitems.util.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleListener;
import org.bukkit.inventory.ItemStack;

/**
 * Handles boat breaking logic.
 *
 * @author narlock
 */
public class EnhancedBoatListener extends VehicleListener {
    private final UsefulItems plugin;
    private final boolean boatsDropEnabled;

    public EnhancedBoatListener(UsefulItems plugin) {
        this.plugin = plugin;
        boatsDropEnabled = plugin.getConfigManager().getRawConfig()
                .getBoolean("boats-drop-boat", true);
    }

    @Override
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        // Exit if boat drops are disabled
        if (!boatsDropEnabled) {
            return;
        }

        if (!(event.getAttacker() instanceof Player)) {
            return; // only handle player actions
        }

        Player player = (Player) event.getAttacker();

        // Exit if decrafting boats are enabled and broke with Gold Axe
        ItemStack heldItem = player.getItemInHand();
        if(heldItem.getTypeId() == Utils.GoldToolType.GOLD_AXE.getId()
                && plugin.getConfigManager().getRawConfig()
                        .getBoolean("decrafter-blocks.boat", true)) {
            return;
        }

        if(event.getVehicle() instanceof Boat) {
            event.setCancelled(true);

            // Drop Boat Item
            event.getVehicle().getWorld().dropItemNaturally(event.getVehicle().getLocation(),
                    new ItemStack(Material.BOAT, 1));

            event.getVehicle().remove();
        }
    }
}
