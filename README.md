# UsefulItems
There are a small amount of different items and blocks within Minecraft Beta 1.7.3 compared to the modern version of the game. UsefulItems is a server-side plugin for Bukkit to give more use to different items.

## Installation

### Option 1: JAR Download (Non-technical)
The [Releases](https://github.com/narlock/UsefulItems/releases) page on GitHub will provide versioned JAR files based on the version of the plugin.
1. Download the `UsefulItems.jar` file from the version of your choice.
2. Drop the `UsefulItems.jar` file into your `plugins` directory of your server.
3. Reload/Restart your server.

### Option 2: Build the JAR Yourself (Technical)
1. Clone the GitHub Repository.
2. Add your CraftBukkit's JAR file as a library.
3. Build the JAR file.
4. Drag the JAR file into your `plugins` directory of your server.
5. Reload/Restart your server.

Upon reload/restart, you will see a message similar to this in your server's log:
```
[INFO] [UsefulItems] UsefulItems X.X enabled.
```
To verify installation, check that "UsefulItems" appears in your plugins list by using the `/plugins` command while in-game.

## Features
This plugin contains features to give more purpose to different items in the game. It also adds some level of enhancements. All settings for this plugin can be toggled in the `config.yml` that lies in the data folder.

### Golden Tools
In my experience playing any version of Minecraft, I have rarely crafted tools with Gold. The main reason for this is that they break easily and provide little value when compared to even the cheaper, Stone pickaxe. That and Iron and Diamond tools are usually the preferred anyways. This plugin hopes to provide special abilities to these tools, encouraging players to craft them, despite their durability remaining the same.

#### "Decrafters"

Gold tools will now serve as "decrafters" in the sense that when they break a block that has been "crafted", a "decrafted" variant will be dropped instead of that block (with some reason, for example, we aren't going to shape a wood plank back into a log). The list includes:
- Dispenser → 7 Cobblestone, 1 Bow, 1 Redstone Dust (when a Gold Pickaxe is used)
- Sandstone → 4 Sand (when a Gold Pickaxe is used)
- Noteblock → 8 Planks, 1 Redstone Dust (when a Gold Axe is used)
- Bed → 3 Wool, 3 Planks (when a Gold Axe is used)
- Sticky Piston → 1 Piston, 1 Slimeball (when a Gold Axe is used)
- Piston → 3 Planks, 4 Cobblestone, 1 Iron Ingot, 1 Redstone Dust (when a Gold Axe is used)
- Workbench → 4 Planks (when a Gold Axe is used)
- Furnace → 8 Cobblestone (when a Gold Pickaxe is used)
- Redstone Torch → 1 Redstone Dust, 1 Stick (when a Gold Axe is used)
- Door → 6 Planks (when a Gold Axe is used)
- Lever → 1 Stick, 1 Cobblestone (when a Gold Axe is used)
- Pressure Plate (Stone) → 2 Stone (when a Gold Pickaxe is used)
- Iron Door → 6 Iron Ingots (when a Gold Pickaxe is used)
- Pressure Plate (Wood) → 2 Planks (when a Gold Axe is used)
- Button → 2 Stone (when a Gold Pickaxe is used)
- Jukebox → 8 Planks, 1 Diamond (when a Gold Axe is used)
- Jack 'o' Lantern → 1 Pumpkin, 1 Torch (when a Gold Axe is used)
- Trapdoor* → 3 Planks (when a Gold Axe is used)
- Fence* → 3 Sticks (when a Gold Axe is used)
- Sign → 6 Planks, 1 Stick (when a Gold Axe is used)
- Redstone Repeater → 3 Stone, 2 Redstone Torches, 1 Redstone Dust (when a Gold Pickaxe is used)
- Brick Block → 4 Bricks (when a Gold Pickaxe is used)
- Gold Block → 9 Gold Ingots (when a Gold Pickaxe is used)
- Iron Block → 9 Iron Ingots (when a Gold Pickaxe is used)
- Diamond Block → 9 Diamonds (when a Gold Pickaxe is used)
- Boat → 5 Planks (when a Gold Axe is used)
- Minecart → 5 Iron Ingots (when a Gold Pickaxe is used)

**Exclusions:**
1. Any craftable item that returns more than one of the crafted item. (ex: crafting torches with 1 stick and 1 coal will give 4 torches) *exception to this includes the trap door, and fences as there is an even divisible amount for a trap door = 3 planks, and fence = 6 sticks.
2. Paintings are currently not included...(yet?)

<p align="center">
  <img src="/demo/decraft_demo.gif" alt="Decraft Demo">
</p>

#### Silk Touch

Enchantments don't exist in Beta 1.7.3, but that doesn't mean we can't add a feature server-side that allows Gold tools to also serve this purpose. The following items when mined with Gold tools will drop themselves:
- Stone (when a Gold Pickaxe is used)
- Coal Ore Block (when a Gold Pickaxe is used)
- Glass (when a Gold Pickaxe is used)
- Lapis Ore Block (when a Gold Pickaxe is used)
- Cobweb (when a Gold Sword is used)
- Bookshelf (when a Gold Axe is used)
- Tiled Dirt (when a Gold Hoe is used)
- Diamond Ore Block (when a Gold Pickaxe is used)
- Redstone Ore Block (when a Gold Pickaxe is used)
- Snow Slab (when a Gold Shovel is used)
- Ice Block (when a Gold Pickaxe is used)
- Snow Block (when a Gold Shovel is used)
- Glowstone (when a Gold Pickaxe is used)

**Exclusions:**
- Spawners could be dropped, but unfortunately when a user places a spawner in Beta 1.7.3, it will always be a Pig Spawner.
- Ferns and Tall Grass also suffer a similar case. When a player holds the item, it will always place a fern. Meaning if someone silk touched a piece of tall grass, it would place as a fern.

<p align="center">
  <img src="/demo/silktouch_demo.gif" alt="Silk Touch Demo">
</p>

### Extended Crafting

Previously unobtainable items can be crafted. The user will use the `/craft` command followed by the name of the item.
- `doublestoneslab` will craft a Double Stone Slab, where the recipe is 2 Stone Slabs and 1 Clay Ball.
- `sponge` will craft a Sponge Block, where the recipe is 2 Yellow Wool, 2 Clay Blocks, a Water Bucket, and 4 Slimeballs (intentionally expensive!)
- `mossycobble` will craft Mossy Cobblestone, where the user can craft 4 blocks minimum at a time, where the recipe is 4 Cobblestone, 2 Leaf Blocks, and 2 Slimeballs.
- `fire` will craft Fire, where the recipe is 3 gunpowder, 3 Netherrack, 2 Glowstone Dust, and 1 Flint (Allows Chainmail Armor to be craftable!)
- `cobweb` will craft Cobweb, where the recipe is 9 string.

<p align="center">
  <img src="/demo/craft_demo.gif" alt="Extended Crafting Demo">
</p>

### Enhanced Item Uses

Similar to the Gold tools, I wanted to enhance what some items can use. For example, the Milk Bucket is only used for crafting Cake in Beta 1.7.3 since brewing does not exist yet. Here are the ideas I came up with for this plugin:
- Milk Bucket: Drinking the milk bucket will give the player God mode for 5 seconds. This means they will not take any damage until the period expires (could possibily display block breaking particle packets to display that the user is in god mode)

<p align="center">
  <img src="/demo/milk_god_mode_demo.gif" alt="Milk God Mode Demo">
</p>

- Chainmail Armor: When the player has full chainmail armor equipped, they will be granted protection from Fire damage (Damage Causes of `FIRE`, `FIRE_TICK`, and `LAVA`)

<p align="center">
  <img src="/demo/chain_fire_res_demo.gif" alt="Chain Armor Fire Resistance Demo">
</p>

- Sugar: A player can right-click with sugar in their hand, they will "eat" the sugar and be granted with suffocation protection for 3 seconds (Damage Causes of `SUFFOCATION` and `DROWNING`). This protection can be stacked. The player's protection time remaining will be shown to them when they consume sugar. A message will appear when the protection is over. (Useful for building underwater!)

<p align="center">
  <img src="/demo/sugar_suffocation_res_demo.gif" alt="Sugar Suffocation Protection Demo">
</p>

- Feather: A player can right-click with feathers in their hand, they will "use" the feather and be granted with fall-damage protection for 2 seconds. This protection can be stacked. The player's protection time remaining will be shown to them when they use feathers. A message will appear when the protection is over. (Useful for deep falls!)

<p align="center">
  <img src="/demo/feather_fall_res_demo.gif" alt="Feather Fall Protection Demo">
</p>

- Bookshelf: Placed bookshelf blocks can have a custom user input string associated to them. When a user right-clicks the bookshelf, the message will be displayed! Server admins can also blacklist words so that users don't break server rules. Blacklisted words will appear as "*" characters. (Blacklisting words is not yet implemented!)

Bookshelf Data is stored in the `bookshelf` directory. Each world will have a JSON file associated to them containing all the bookshelves with notes. A sample file may look like this:
```json
{
  "bookshelves": [
    {
      "location": {
        "x": 100,
        "y": 65,
        "z": -200
      },
      "message": "This is a demo message of the bookshelf note!",
      "owner": "narlock",
      "protected": true
    },
    {
      "location": {
        "x": 50,
        "y": 45,
        "z": 100
      },
      "message": "This is another demo message!",
      "owner": "narlock",
      "protected": true
    }
  ]
}
```

<p align="center">
  <img src="/demo/bookshelf_note_demo.gif" alt="Bookshelf Note Demo">
</p>

- Fireball: More use of a Gold Sword, when a player has the Fire block in their inventory, they can right-click a Gold Sword to cast a fireball. This is similar to a Ghast fireball. There is a 5-second cooldown on Fireballs by default.

<p align="center">
  <img src="/demo/fireball_cast_demo.gif" alt="Fireball Demo">
</p>

- Dispenser Fireball: If Fire block is in the dispenser, the dispenser will shoot a fireball.

<p align="center">
  <img src="/demo/dispenser_fireball_demo.gif" alt="Dispenser Fireball Demo">
</p>

### Other Enhancements
- When stairs are broken, they will drop the actual stair block. Stone stairs will only drop when using a Pickaxe.

<p align="center">
  <img src="/demo/stair_break_demo.gif" alt="Stair Break Demo">
</p>

- When boats are broken, they will drop the boat item instead of breaking into planks and sticks.

<p align="center">
  <img src="/demo/boat_break_demo.gif" alt="Boat Break Demo">
</p>

- Option to stop ice from melting (since they can be obtained and used in builds).

### Known Conflicts
The following section contains other plugin conflicts that I have discovered while testing UsefulItems. This section offers future opportunities to help allow UsefulItems to work nicely with other plugins.

1. When using this plugin with [Dynmap](https://github.com/webbukkit/Dynmap), blocks mined with Decraft, Silk, or stair-breaking features will not update the web map.
