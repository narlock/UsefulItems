package com.narlock.usefulitems.util;

import org.bukkit.entity.Player;

public class PermissionsUtil {
    public static final String PREFIX = "usefulitems.";

    public static boolean canDecraft(Player player) {
        return player.hasPermission(PREFIX + "decrafter");
    }

    public static boolean canSilkTouch(Player player) {
        return player.hasPermission(PREFIX + "silktouch");
    }

    public static boolean canExtendedCraft(Player player) {
        return player.hasPermission(PREFIX + "extendedcrafting");
    }

    public static boolean canEnhancedItems(Player player) {
        return player.hasPermission(PREFIX + "enhanceditems");
    }

    public static boolean canCraftCommand(Player player) {
        return player.hasPermission(PREFIX + "craft");
    }

    public static boolean canReload(Player player) {
        return player.hasPermission(PREFIX + "reload");
    }

    public static boolean canUseBookshelfNotes(Player player) {
        return player.hasPermission(PREFIX + "note");
    }
}
