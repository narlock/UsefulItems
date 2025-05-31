package com.narlock.usefulitems.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    public static final String TITLE = "[UsefulItems] ";
    private static final Map<String, Integer> BLOCK_NAME_TO_ID = new HashMap<>();

    static {
        // Decrafter blocks
        BLOCK_NAME_TO_ID.put("dispenser", 23);
        BLOCK_NAME_TO_ID.put("sandstone", 24);
        BLOCK_NAME_TO_ID.put("noteblock", 25);
        BLOCK_NAME_TO_ID.put("bed", 26);
        BLOCK_NAME_TO_ID.put("sticky_piston", 29);
        BLOCK_NAME_TO_ID.put("piston", 33);
        BLOCK_NAME_TO_ID.put("workbench", 58);
        BLOCK_NAME_TO_ID.put("furnace", 61);
        BLOCK_NAME_TO_ID.put("redstone_torch", 76);
        BLOCK_NAME_TO_ID.put("wooden_door", 64);
        BLOCK_NAME_TO_ID.put("lever", 69);
        BLOCK_NAME_TO_ID.put("stone_pressure_plate", 70);
        BLOCK_NAME_TO_ID.put("iron_door", 71);
        BLOCK_NAME_TO_ID.put("wooden_pressure_plate", 72);
        BLOCK_NAME_TO_ID.put("stone_button", 77);
        BLOCK_NAME_TO_ID.put("jukebox", 84);
        BLOCK_NAME_TO_ID.put("jack_o_lantern", 91);
        BLOCK_NAME_TO_ID.put("trapdoor", 96);
        BLOCK_NAME_TO_ID.put("fence", 85);
        BLOCK_NAME_TO_ID.put("sign_post", 63);
        BLOCK_NAME_TO_ID.put("wall_sign", 68);
        BLOCK_NAME_TO_ID.put("redstone_repeater_off", 93);
        BLOCK_NAME_TO_ID.put("redstone_repeater_on", 94);
        BLOCK_NAME_TO_ID.put("double_stone_slab", 43);
        BLOCK_NAME_TO_ID.put("brick_block", 45);
        BLOCK_NAME_TO_ID.put("gold_block", 41);
        BLOCK_NAME_TO_ID.put("iron_block", 42);
        BLOCK_NAME_TO_ID.put("diamond_block", 57);

        // Silktouch blocks
        BLOCK_NAME_TO_ID.put("stone", 1);
        BLOCK_NAME_TO_ID.put("coal_ore", 16);
        BLOCK_NAME_TO_ID.put("glass", 20);
        BLOCK_NAME_TO_ID.put("lapis_ore", 21);
        BLOCK_NAME_TO_ID.put("cobweb", 30);
        BLOCK_NAME_TO_ID.put("bookshelf", 47);
        BLOCK_NAME_TO_ID.put("tilled_dirt", 60);
        BLOCK_NAME_TO_ID.put("diamond_ore", 56);
        BLOCK_NAME_TO_ID.put("redstone_ore", 73);
        BLOCK_NAME_TO_ID.put("snow_layer", 78);
        BLOCK_NAME_TO_ID.put("ice", 79);
        BLOCK_NAME_TO_ID.put("snow_block", 80);
        BLOCK_NAME_TO_ID.put("glowstone", 89);
    }

    public enum GoldToolType {
        GOLD_PICKAXE(285),
        GOLD_AXE(286),
        GOLD_SHOVEL(284),
        GOLD_HOE(294),
        GOLD_SWORD(283);

        private final int id;

        GoldToolType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static boolean matches(GoldToolType toolType, int itemId) {
            return toolType.getId() == itemId;
        }
    }

    public static String BLOCK_NAME_BY_ID(int blockId) {
        for (Map.Entry<String, Integer> entry : BLOCK_NAME_TO_ID.entrySet()) {
            if (entry.getValue() == blockId) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static void applyToolDurability(Player player, ItemStack heldItem) {
        short newDurability = (short) (heldItem.getDurability() + 1);
        if (newDurability >= heldItem.getType().getMaxDurability()) {
            player.setItemInHand(null); // tool breaks
        } else {
            heldItem.setDurability(newDurability);
            player.setItemInHand(heldItem);
        }
    }

    public static final List<Integer> PICKAXE_TOOL_IDS = Arrays.asList(257, 270, 274, 278, 285);

    public static boolean isTool(ItemStack item) {
        if (item == null) return false;

        Material type = item.getType();

        return type.name().endsWith("_PICKAXE")
                || type.name().endsWith("_AXE")
                || type.name().endsWith("_SHOVEL")
                || type.name().endsWith("_SWORD")
                || type.name().endsWith("_HOE");
    }
}
