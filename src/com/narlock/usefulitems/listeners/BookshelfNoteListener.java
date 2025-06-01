package com.narlock.usefulitems.listeners;

import com.narlock.usefulitems.UsefulItems;
import com.narlock.usefulitems.config.BookshelfNoteManager;
import com.narlock.usefulitems.util.PermissionsUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

/**
 * Handles bookshelf right-click note messages
 *
 * @author narlock
 */
public class BookshelfNoteListener extends PlayerListener {
    private final UsefulItems plugin;
    private final BookshelfNoteManager noteManager;

    public BookshelfNoteListener(UsefulItems plugin) {
        this.plugin = plugin;
        this.noteManager = plugin.getBookshelfNoteManager();
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() != Material.BOOKSHELF) return;

        Player player = event.getPlayer();

        // Exit if player does not have permission
        if (!PermissionsUtil.canUseBookshelfNotes(player)) {
            return;
        }

        Block bookshelf = event.getClickedBlock();
        String note = noteManager.getNoteAt(bookshelf.getLocation());
        if (note != null) {
            player.sendMessage(note);
        } else {
            player.sendMessage("§7This bookshelf has no note.");
        }
    }
}
