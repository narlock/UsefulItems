package com.narlock.usefulitems.commands;

import com.narlock.usefulitems.UsefulItems;
import com.narlock.usefulitems.config.BookshelfNoteManager;
import com.narlock.usefulitems.config.ConfigManager;
import com.narlock.usefulitems.util.PermissionsUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles /note command
 *
 * @author narlock
 */
public class BookshelfNoteCommand implements CommandExecutor {
    private final UsefulItems plugin;
    private final BookshelfNoteManager noteManager;

    public BookshelfNoteCommand(UsefulItems plugin) {
        this.plugin = plugin;
        this.noteManager = plugin.getBookshelfNoteManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!PermissionsUtil.canUseBookshelfNotes(player)) {
            player.sendMessage("§cYou don't have permission to use bookshelf notes!");
            return true;
        }

        if (args.length < 1) {
            sendUsageMessage(player);
            return true;
        }

        Block target = player.getTargetBlock(null, 5);
        if (target == null || target.getType() != Material.BOOKSHELF) {
            player.sendMessage("§cYou must be looking at a bookshelf.");
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "remove":
                handleRemove(player, target);
                break;

            case "protect":
                handleToggleProtection(player, target);
                break;

            // Set the note on the bookshelf
            default:
                String message = String.join(" ", args);
                handleSetNote(player, target, message);
                break;
        }

        return true;
    }

    private void handleSetNote(Player player, Block bookshelf, String message) {
        noteManager.setNoteAt(bookshelf.getLocation(), message, player.getName(), false);
        player.sendMessage("§aNote set on this bookshelf.");
    }

    private void handleRemove(Player player, Block bookshelf) {
        boolean success = noteManager.removeNoteAt(bookshelf.getLocation(), player.getName(), player.isOp());
        if (success) {
            player.sendMessage("§aNote removed from this bookshelf.");
        } else {
            player.sendMessage("§cYou are not allowed to remove this protected note.");
        }
    }

    private void handleToggleProtection(Player player, Block bookshelf) {
        boolean toggled = noteManager.toggleProtectionAt(bookshelf.getLocation(), player.getName(), player.isOp());
        if (toggled) {
            player.sendMessage("§aBookshelf protection toggled.");
        } else {
            player.sendMessage("§cYou are not allowed to modify protection on this bookshelf.");
        }
    }

    private void sendUsageMessage(Player player) {
        player.sendMessage("§cUsage: /note <message>");
        player.sendMessage("§cRemove note: /note remove");
        player.sendMessage("§cToggle protection: /note protect");
    }
}
