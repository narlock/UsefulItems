package com.narlock.usefulitems.listeners;

import com.narlock.usefulitems.UsefulItems;
import com.narlock.usefulitems.config.ConfigManager;
import org.bukkit.Material;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockListener;

public class StopIceMeltListener extends BlockListener {
    private final ConfigManager configManager;

    public StopIceMeltListener(UsefulItems plugin) {
        this.configManager = plugin.getConfigManager();
    }

    @Override
    public void onBlockFade(BlockFadeEvent event) {
        if (configManager.isStopIceMelt()) {
            if (event.getBlock().getType() == Material.ICE) {
                event.setCancelled(true);
            }
        }
    }
}
