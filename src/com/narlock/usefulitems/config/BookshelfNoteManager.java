package com.narlock.usefulitems.config;

import com.narlock.usefulitems.UsefulItems;
import org.bukkit.Location;
import org.bukkit.World;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Manages the bookshelf note data
 *
 * @author narlock
 */
public class BookshelfNoteManager {
    private static final Logger logger = Logger.getLogger("Minecraft.UsefulItems");
    private final UsefulItems plugin;
    private final File bookshelvesFolder;

    public BookshelfNoteManager(UsefulItems plugin) {
        this.plugin = plugin;
        this.bookshelvesFolder = new File(plugin.getDataFolder(), "bookshelves");

        // Ensure that the bookshelf directory is present
        if (!bookshelvesFolder.exists()) bookshelvesFolder.mkdirs();
    }

    /**
     * Retrieves the world file from the bookshelves folder
     * @param world the Minecraft world
     * @return the bookshelf data file associated to the world
     */
    private File getWorldFile(World world) {
        return new File(bookshelvesFolder, world.getName() + ".json");
    }

    /**
     * Retrieves the note message on a bookshelf
     * @param loc The location of the bookshelf
     * @return The note associated to the bookshelf.
     *         Null if no message on the bookshelf.
     */
    public String getNoteAt(Location loc) {
        JSONArray bookshelves = loadWorldData(loc.getWorld());
        for (Object obj : bookshelves) {
            JSONObject entry = (JSONObject) obj;
            JSONObject location = (JSONObject) entry.get("location");

            long x = (long) location.get("x");
            long y = (long) location.get("y");
            long z = (long) location.get("z");

            if (x == loc.getBlockX() && y == loc.getBlockY() && z == loc.getBlockZ()) {
                return (String) entry.get("message");
            }
        }
        return null;
    }

    /**
     * Sets the note on the given bookshelf location
     * @param loc Location of the bookshelf
     * @param message Message to set on the bookshelf
     * @param ownerName The note "setter"
     * @param isProtected Flag to set protection
     */
    public void setNoteAt(Location loc, String message, String ownerName, boolean isProtected) {
        // Load bookshelves array from world file
        JSONArray bookshelves = loadWorldData(loc.getWorld());

        // Re-set the bookshelf note if a bookshelf note already existed in the world
        boolean found = false;
        for (Object obj : bookshelves) {
            JSONObject entry = (JSONObject) obj;
            JSONObject location = (JSONObject) entry.get("location");

            long x = (long) location.get("x");
            long y = (long) location.get("y");
            long z = (long) location.get("z");

            if (x == loc.getBlockX() && y == loc.getBlockY() && z == loc.getBlockZ()) {
                entry.put("message", message);
                entry.put("owner", ownerName);
                entry.put("protected", isProtected);
                found = true;
                break;
            }
        }

        // Set the bookshelf note in the world
        if (!found) {
            JSONObject newEntry = new JSONObject();
            JSONObject locObj = new JSONObject();
            locObj.put("x", loc.getBlockX());
            locObj.put("y", loc.getBlockY());
            locObj.put("z", loc.getBlockZ());

            newEntry.put("location", locObj);
            newEntry.put("message", message);
            newEntry.put("owner", ownerName);
            newEntry.put("protected", isProtected);

            bookshelves.add(newEntry);
        }

        // Save the world bookshelf file to server disk
        saveWorldData(loc.getWorld(), bookshelves);
    }

    /**
     * Removes the note from a bookshelf
     * @param loc Location of the bookshelf in the world
     * @param requesterName User calling for the removal of the note
     * @param isOp Is the requester a server operator
     * @return true if the note was removed, false if it wasn't
     */
    public boolean removeNoteAt(Location loc, String requesterName, boolean isOp) {
        // Load bookshelves array from world file
        JSONArray bookshelves = loadWorldData(loc.getWorld());

        // Remove the entry from disk, if possible
        boolean removed = bookshelves.removeIf(obj -> {
            JSONObject entry = (JSONObject) obj;
            JSONObject location = (JSONObject) entry.get("location");

            long x = (long) location.get("x");
            long y = (long) location.get("y");
            long z = (long) location.get("z");

            if (x == loc.getBlockX() && y == loc.getBlockY() && z == loc.getBlockZ()) {
                boolean isProtected = (boolean) entry.getOrDefault("protected", false);
                String owner = (String) entry.get("owner");

                // If protected, only the owner and server operators can remove the note.
                // If not protected, any user can remove the note.
                return !isProtected || requesterName.equals(owner) || isOp;
            }
            return false;
        });

        // Save the update to disk if successfully removed
        if (removed) {
            saveWorldData(loc.getWorld(), bookshelves);
        }

        return removed;
    }

    /**
     * Toggles note protection on a bookshelf
     * @param loc Location of the bookshelf in the world
     * @param requesterName User calling to toggle protection on the note
     * @param isOp Is the requester a server operator
     * @return true if the note protection was toggled
     */
    public boolean toggleProtectionAt(Location loc, String requesterName, boolean isOp) {
        // Load bookshelves array from world file
        JSONArray bookshelves = loadWorldData(loc.getWorld());

        // Toggle the note entry from disk, if possible
        boolean toggled = false;
        for (Object obj : bookshelves) {
            JSONObject entry = (JSONObject) obj;
            JSONObject location = (JSONObject) entry.get("location");

            long x = (long) location.get("x");
            long y = (long) location.get("y");
            long z = (long) location.get("z");

            if (x == loc.getBlockX() && y == loc.getBlockY() && z == loc.getBlockZ()) {
                String owner = (String) entry.get("owner");

                // Only owner of note or server operators can toggle note protection
                if (requesterName.equals(owner) || isOp) {
                    boolean current = (boolean) entry.getOrDefault("protected", false);
                    entry.put("protected", !current);
                    toggled = true;
                    break;
                }
            }
        }

        // Save the update to disk if successfully toggled
        if (toggled) {
            saveWorldData(loc.getWorld(), bookshelves);
        }

        return toggled;
    }


    /**
     * Loads the bookshelf data file for the world
     * @param world Minecraft world
     * @return The JSON contents of the bookshelf data for the world
     */
    private JSONArray loadWorldData(World world) {
        File file = getWorldFile(world);

        // If the file doesn't exist, return empty JSON array
        if (!file.exists()) return new JSONArray();

        // Read and return the bookshelves JSON array
        try (FileReader reader = new FileReader(file)) {
            JSONObject root = (JSONObject) new JSONParser().parse(reader);
            JSONArray bookshelves = (JSONArray) root.get("bookshelves");

            // If for some reason there isn't a bookshelves array, return empty JSON array
            return bookshelves != null ? bookshelves : new JSONArray();
        } catch (Exception e) {
            logger.warning("Failed to load bookshelf data for world " + world.getName() + ": " + e.getMessage());
            return new JSONArray();
        }
    }

    /**
     * Saves the bookshelf data associated to the world
     * @param world The Minecraft world
     * @param bookshelves JSON array associated to the Minecraft world
     */
    private void saveWorldData(World world, JSONArray bookshelves) {
        // Retrieve the world file to save to
        File file = getWorldFile(world);

        // Put the bookshelves array into the root JSON object
        JSONObject root = new JSONObject();
        root.put("bookshelves", bookshelves);

        // Write the file to disk
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(root.toJSONString());
        } catch (IOException e) {
            logger.severe("Failed to save bookshelf data for world " + world.getName() + ": " + e.getMessage());
        }
    }
}
