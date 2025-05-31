package com.narlock.usefulitems;

import com.narlock.usefulitems.config.ConfigManager;
import com.narlock.usefulitems.listeners.DecrafterListener;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

import static com.narlock.usefulitems.util.Constants.TITLE;

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
//            pluginManager.registerEvent(
//                    Event.Type.BLOCK_BREAK, new DecrafterListener(this), Event.Priority.Normal, this);
            logger.info(TITLE + "Decrafter Feature enabled.");
        } else {
            logger.warning(TITLE + "Decrafter Feature is disabled.");
        }

        logger.info(TITLE + "UsefulItems " + getDescription().getVersion() + " enabled.");
    }

    @Override
    public void onDisable() {
        logger.info(TITLE + "UsefulItems " + getDescription().getVersion() + " disabled.");
    }
}