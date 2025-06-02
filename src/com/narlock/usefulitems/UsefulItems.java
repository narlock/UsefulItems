package com.narlock.usefulitems;

import com.narlock.usefulitems.commands.BookshelfNoteCommand;
import com.narlock.usefulitems.commands.CraftCommand;
import com.narlock.usefulitems.commands.ReloadCommand;
import com.narlock.usefulitems.config.BookshelfNoteManager;
import com.narlock.usefulitems.config.ConfigManager;
import com.narlock.usefulitems.listeners.*;
import com.narlock.usefulitems.util.CooldownManager;
import com.narlock.usefulitems.util.ProtectionManager;
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
    private ProtectionManager protectionManager;
    private BookshelfNoteManager bookshelfNoteManager;
    private CooldownManager cooldownManager;

    @Override
    public void onEnable() {
        // Create plugin configuration directories if they don't exist yet
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // Initialize configuration manager
        this.configManager = new ConfigManager(this);
        this.bookshelfNoteManager = new BookshelfNoteManager(this);
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

        // Register Enhanced Item Listener
        if(configManager.isFeatureEnabled("enhancedItems")) {
            this.protectionManager = new ProtectionManager(this);
            this.cooldownManager = new CooldownManager(this);
            pluginManager.registerEvent(Event.Type.PLAYER_INTERACT,
                    new EnhancedItemPlayerListener(this), Event.Priority.Normal, this);
            pluginManager.registerEvent(Event.Type.ENTITY_DAMAGE,
                    new EnhancedItemDamageListener(this), Event.Priority.Normal, this);
            pluginManager.registerEvent(Event.Type.PLAYER_INTERACT,
                    new FireballCastingListener(this), Event.Priority.Normal, this);
            pluginManager.registerEvent(Event.Type.BLOCK_DISPENSE,
                    new FireballDispenserListener(), Event.Priority.Normal, this);
            logger.info(TITLE + "Enhanced Items Feature is enabled.");
        } else {
            logger.warning(TITLE + "Enhanced Items Feature is disabled.");
        }

        // Register Bookshelf Note Feature
        if(configManager.isFeatureEnabled("bookshelfnotes")) {
            getCommand("note").setExecutor(new BookshelfNoteCommand(this));
            pluginManager.registerEvent(Event.Type.PLAYER_INTERACT,
                    new BookshelfNoteListener(this), Event.Priority.Normal, this);
            pluginManager.registerEvent(Event.Type.BLOCK_BREAK,
                    new BookshelfNoteBlockListener(this), Event.Priority.Normal, this);
            logger.info(TITLE + "Bookshelf Notes Feature enabled.");
        } else {
            logger.warning(TITLE + "Bookshelf Notes Feature is disabled.");
        }

        // Register Enhanced Block listener
        pluginManager.registerEvent(Event.Type.BLOCK_BREAK,
                new EnhancedBlockListener(this), Event.Priority.Normal, this);
        pluginManager.registerEvent(Event.Type.VEHICLE_DESTROY,
                new EnhancedBoatListener(this), Event.Priority.Normal, this);
        pluginManager.registerEvent(Event.Type.BLOCK_FADE,
                new StopIceMeltListener(this), Event.Priority.Normal, this);

        // Register /reloadusefulitems
        getCommand("reloadusefulitems").setExecutor(new ReloadCommand(this));

        logger.info(TITLE + "UsefulItems " + getDescription().getVersion() + " finished enabling.");
    }

    @Override
    public void onDisable() {
        logger.info(TITLE + "UsefulItems " + getDescription().getVersion() + " disabled.");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ProtectionManager getProtectionManager() {
        return protectionManager;
    }

    public BookshelfNoteManager getBookshelfNoteManager() {
        return bookshelfNoteManager;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }
}