package com.narlock.usefulitems.commands;

import com.narlock.usefulitems.UsefulItems;
import com.narlock.usefulitems.config.ConfigManager;
import com.narlock.usefulitems.util.PermissionsUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

import static com.narlock.usefulitems.util.Utils.TITLE;

/**
 * Handles /reloadusefulitems command
 *
 * @author narlock
 */
public class ReloadCommand implements CommandExecutor {
    private static final Logger logger = Logger.getLogger("Minecraft.UsefulItems");
    private final UsefulItems plugin;
    private final ConfigManager configManager;

    public ReloadCommand(UsefulItems plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!PermissionsUtil.canReload(player)) {
            player.sendMessage("§cYou don't have access to this command.");
            return true;
        }

        // Reload the configuration
        configManager.reload();

        // Send confirmation
        player.sendMessage("§aUsefulItems configuration reloaded successfully.");
        logger.info(TITLE + "[UsefulItems] Configuration reloaded by " + player.getName());

        return true;
    }
}
