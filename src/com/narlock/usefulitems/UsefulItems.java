package com.narlock.usefulitems;

import com.narlock.usefulitems.commands.CraftCommand;
import com.narlock.usefulitems.config.ConfigManager;
import com.narlock.usefulitems.listeners.*;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

import static com.narlock.usefulitems.util.Utils.TITLE;

/**
 * Main plugin class
 *
 * @author narlock
 */
public class UsefulItems extends JavaPlugin {
    private static final Logger logger = Logger.getLogger("Minecraft.UsefulItems");
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        // Create plugin configuration directories if they don't exist yet
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // Initialize configuration manager
        this.configManager = new ConfigManager(this);
        PluginManager pluginManager = getServer().getPluginManager();

        // Register decrafter feature if enabled
        if (configManager.isFeatureEnabled("decrafter")) {
            pluginManager.registerEvent(Event.Type.BLOCK_BREAK,
                    new DecrafterListener(this), Event.Priority.Normal, this);
            pluginManager.registerEvent(Event.Type.VEHICLE_DESTROY,
                    new DecrafterVehicleListener(this), Event.Priority.Normal, this);
            logger.info(TITLE + "Decrafter Feature enabled.");
        } else {
            logger.warning(TITLE + "Decrafter Feature is disabled.");
        }

        // Register silk touch feature if enabled
        if (configManager.isFeatureEnabled("silktouch")) {
            pluginManager.registerEvent(Event.Type.BLOCK_BREAK,
                    new SilkTouchListener(this), Event.Priority.Normal, this);
            logger.info(TITLE + "Silk Touch Feature enabled.");
        } else {
            logger.warning(TITLE + "Silk Touch Features is disabled.");
        }

        // Register /craft command executor
        if (configManager.isFeatureEnabled("extendedcrafting")) {
            getCommand("craft").setExecutor(new CraftCommand(this));
            logger.info(TITLE + "Extended Crafting Feature enabled.");
        } else {
            logger.warning(TITLE + "Extended Crafting Feature is disabled.");
        }

        // TODO Register Enhanced Item Listener

        // Register enhanced block listener
        pluginManager.registerEvent(Event.Type.BLOCK_BREAK,
                new EnhancedBlockListener(this), Event.Priority.Normal, this);
        pluginManager.registerEvent(Event.Type.VEHICLE_DESTROY,
                new EnhancedBoatListener(this), Event.Priority.Normal, this);

        logger.info(TITLE + "UsefulItems " + getDescription().getVersion() + " finished enabling.");
    }

    @Override
    public void onDisable() {
        logger.info(TITLE + "UsefulItems " + getDescription().getVersion() + " disabled.");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}