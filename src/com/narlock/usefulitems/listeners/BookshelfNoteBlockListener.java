package com.narlock.usefulitems.listeners;

import com.narlock.usefulitems.UsefulItems;
import com.narlock.usefulitems.config.BookshelfNoteManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

import java.util.logging.Logger;

import static com.narlock.usefulitems.util.Utils.TITLE;

/**
 * Handles removing bookshelf notes when a bookshelf with a note is destroyed.
 *
 * @author narlock
 */
public class BookshelfNoteBlockListener extends BlockListener {
    private static final Logger logger = Logger.getLogger("Minecraft.UsefulItems");
    private final UsefulItems plugin;
    private final BookshelfNoteManager noteManager;

    public BookshelfNoteBlockListener(UsefulItems plugin) {
        this.plugin = plugin;
        this.noteManager = plugin.getBookshelfNoteManager();
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location blockLocation = block.getLocation();

        if (block.getType() == Material.BOOKSHELF && noteManager.getNoteAt(blockLocation) != null) {
            // Remove the note from the bookshelf regardless of permission (isOp = true)
            noteManager.removeNoteAt(blockLocation, player.getName(), true);
            logger.info(TITLE + "Bookshelf note removed at "
                    + blockLocation.getBlockX() + ", "
                    + blockLocation.getBlockY() + ", "
                    + blockLocation.getBlockZ());
        }
    }
}
