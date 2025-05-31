package com.narlock.usefulitems.listeners;

import com.narlock.usefulitems.UsefulItems;
import com.narlock.usefulitems.util.PermissionsUtil;
import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleListener;
import org.bukkit.inventory.ItemStack;

import static com.narlock.usefulitems.util.Utils.applyToolDurability;

/**
 * Handles gold tool decrafting for vehicle entities.
 *
 * @author narlock
 */
public class DecrafterVehicleListener extends VehicleListener {
    private final boolean boatEnabled;
    private final boolean minecartEnabled;

    public DecrafterVehicleListener(UsefulItems plugin) {
        boatEnabled = plugin.getConfigManager().isFeatureEnabled("decrafter")
                && plugin.getConfigManager().getRawConfig().getBoolean("decrafter-blocks.boat", true);
        minecartEnabled = plugin.getConfigManager().isFeatureEnabled("decrafter")
                && plugin.getConfigManager().getRawConfig().getBoolean("decrafter-blocks.minecart", true);
    }

    @Override
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        if (!(event.getAttacker() instanceof Player)) {
            return; // only handle player actions
        }

        Player player = (Player) event.getAttacker();
        ItemStack heldItem = player.getItemInHand();

        if (!PermissionsUtil.canDecraft(player)) {
            return;
        }

        if (event.getVehicle() instanceof Boat && boatEnabled) {
            if (heldItem == null || heldItem.getTypeId() != 286) { // Gold Axe
                return;
            }

            // Cancel default event
            event.setCancelled(true);

            // Drop decrafted materials: 5 wood planks
            event.getVehicle().getWorld().dropItemNaturally(event.getVehicle().getLocation(),
                    new ItemStack(Material.WOOD, 5));

            applyToolDurability(player, heldItem);

            event.getVehicle().remove();
        }

        if (event.getVehicle() instanceof Minecart && minecartEnabled) {
            if (heldItem == null || heldItem.getTypeId() != 285) { // Gold Pickaxe
                return;
            }

            // Cancel default event
            event.setCancelled(true);

            // Drop decrafted materials: 5 iron ingots
            event.getVehicle().getWorld().dropItemNaturally(event.getVehicle().getLocation(),
                    new ItemStack(Material.IRON_INGOT, 5));

            applyToolDurability(player, heldItem);

            event.getVehicle().remove();
        }
    }
}
