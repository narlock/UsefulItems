package com.narlock.usefulitems.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Fireball;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class FireballDispenserListener extends BlockListener {

    @Override
    public void onBlockDispense(BlockDispenseEvent event) {
        Block block = event.getBlock();

        if (block.getType() == Material.DISPENSER && event.getItem().getType() == Material.FIRE) {
            event.setCancelled(true);

            // Consume one fire item from dispenser
            removeFireFromDispenser(block);

            // Get direction that the dispenser is facing
            byte data = block.getData();
            String facing = getFacingName(data);

            // Using the facing direction, spawn the fireball
            Vector direction = getDispenserDirection(facing);
            block.getWorld().spawn(getDispenserShootLocation(block, facing).add(direction.getX(), direction.getY(), direction.getZ()), Fireball.class);
        }
    }

    private void removeFireFromDispenser(Block block) {
        Dispenser dispenser = (Dispenser) block.getState();
        ItemStack[] contents = dispenser.getInventory().getContents();

        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null && item.getType() == Material.FIRE) {
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                } else {
                    contents[i] = null;
                }
                dispenser.getInventory().setContents(contents);
                dispenser.update();
                break;
            }
        }
    }

    private String getFacingName(byte data) {
        switch (data) {
            case 2: return "NORTH";
            case 3: return "SOUTH";
            case 4: return "WEST";
            case 5: return "EAST";
            default: return "UNKNOWN";
        }
    }

    private Vector getDispenserDirection(String facingName) {

        switch (facingName) {
            case "NORTH":
                return new Vector(0, 0, -2);
            case "SOUTH":
                return new Vector(0, 0, 2);
            case "EAST":
                return new Vector(2, 0, 0);
            case "WEST":
                return new Vector(-2, 0, 0);
            default:
                return new Vector(0, 0, 0);
        }
    }

    private Location getDispenserShootLocation(Block block, String facing) {
        World world = block.getWorld();
        double x = block.getX();
        double y = block.getY();
        double z = block.getZ();

        float yaw;
        switch (facing) {
            case "NORTH": yaw = 180f; break;
            case "SOUTH": yaw = 0f;   break;
            case "EAST":  yaw = -90f; break;
            case "WEST":  yaw = 90f;  break;
            default:      yaw = 0f;   break;
        }

        return new Location(world, x, y, z, yaw, 0f);
    }
}
