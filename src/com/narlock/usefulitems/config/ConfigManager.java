package com.narlock.usefulitems.config;

import com.narlock.usefulitems.UsefulItems;
import org.bukkit.util.config.Configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

import static com.narlock.usefulitems.util.Constants.TITLE;

/**
 * Manages blacklist words, configurations
 *
 * @author narlock
 */
public class ConfigManager {
    private static final Logger logger = Logger.getLogger("Minecraft.UsefulItems");

    private final UsefulItems plugin;
    private final File configFile;
    private final Configuration config;

    public ConfigManager(UsefulItems plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");
        ensureConfigExists();

        this.config = new Configuration(configFile);
        this.config.load();
    }

    private void ensureConfigExists() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        if (!configFile.exists()) {
            logger.info(TITLE + "No config.yml found. Copying default from plugin JAR...");

            // Read the default configuration
            try (InputStream in = plugin.getClass().getResourceAsStream("/config.yml")) {
                // Ensure that the configuration file is in the JAR
                if (in == null) {
                    logger.severe(TITLE + "Default config.yml not found inside JAR!");
                    return;
                }

                // Copy the contents of the default configuration to the plugin's config.yml
                Files.copy(in, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            } catch (IOException e) {
                logger.warning(TITLE + "Failed to copy default config.yml: " + e.getMessage());
            }
        }
    }

    public void reload() {
        config.load();
    }

    public void save() {
        config.save();
    }

    public Configuration getRawConfig() {
        return config;
    }

    public boolean isFeatureEnabled(String featureKey) {
        return config.getBoolean("features." + featureKey, true);
    }

    public boolean isDecrafterBlockEnabled(int blockId) {
        if (!isFeatureEnabled("decrafter")) return false;
        return config.getBoolean("decrafter-blocks." + blockId, true);
    }

    public boolean isSilkTouchBlockEnabled(int blockId) {
        if (!isFeatureEnabled("silktouch")) return false;
        return config.getBoolean("silktouch-blocks." + blockId, true);
    }

    public boolean isExtendedCraftingEnabled(String itemKey) {
        if (!isFeatureEnabled("extendedcrafting")) return false;
        return config.getBoolean("extendedcrafting." + itemKey, true);
    }

    public int getEnhancedItemSetting(String itemKey, String settingKey, int defaultValue) {
        return config.getInt("enhanceditems." + itemKey + "." + settingKey, defaultValue);
    }
}
