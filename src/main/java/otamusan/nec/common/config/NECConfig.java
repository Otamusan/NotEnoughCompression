package otamusan.nec.common.config;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import otamusan.nec.common.Lib;

public class NECConfig {
	@Config(modid = Lib.MOD_ID)
	public static class CONFIG_TYPES {
		@Comment({ "Items written here can not be compressed" })
		public static String[] compressedExclusion = {};

		@Comment({ "Catalyst of compression" })
		public static String compressioncatalyst = "minecraft:piston";

		@Comment({ "Catalyst of decompression" })
		public static String decompressioncatalyst = "minecraft:sticky_piston";

		@Comment({ "Blocks written here can not be placed even if it is compressed" })
		public static String[] placeExclusion = {};

		@Comment({ "Number of compression recipes to be displayed on JEI" })
		public static int JEIshowCompressionTime = 5;

		@Comment({ "Restriction of time of compressed Item on using" })
		public static int UsingCompressionTime = 3;

		@Comment({ "Items written here can not use" })
		public static String[] usingExclusion = {};

		@Comment({ "Whether to show the recipe of compression" })
		public static boolean JEIshowCompression = true;

		@Comment({ "Whether to show the recipe of uncompression" })
		public static boolean JEIshowUncompression = true;

		@Comment({ "Whether to show the recipe of compressed crating" })
		public static boolean JEIshowCompressedCraft = true;

		@Comment({ "Whether to replace generated blocks in the world" })
		public static boolean isReplaceBlocks = false;

		@Comment({ "Number of replacements per chunk" })
		public static int replacetime = 10;

		@Comment({ "Maximum compressed time of blocks replaced" })
		public static int maxtimereplaced = 5;

		@Comment({ "Deviation of the replaced compressed time" })
		public static int deviationoftime = 4;
	}

	public static Item getCompressionCatalyst() {
		return Item.getByNameOrId(CONFIG_TYPES.compressioncatalyst);
	}

	public static Item getDecompressionCatalyst() {
		return Item.getByNameOrId(CONFIG_TYPES.decompressioncatalyst);
	}

	public static boolean isCompressionCatalyst(Item item) {
		Item cfgitem = Item.getByNameOrId(CONFIG_TYPES.compressioncatalyst);
		return item == cfgitem;
	}

	public static boolean isDecompressionCatalyst(Item item) {
		Item cfgitem = Item.getByNameOrId(CONFIG_TYPES.decompressioncatalyst);
		return item == cfgitem;
	}

	public static boolean isCompressible(Item item) {
		for (String name : CONFIG_TYPES.compressedExclusion) {
			Item cfgItem = Item.getByNameOrId(name);
			if (item == cfgItem)
				return false;
		}
		return true;
	}

	public static boolean isUsable(Item item) {
		for (String name : CONFIG_TYPES.compressedExclusion) {
			Item cfgItem = Item.getByNameOrId(name);
			if (item == cfgItem)
				return false;
		}
		return true;
	}

	public static boolean isPlacable(Block block) {
		for (String name : CONFIG_TYPES.placeExclusion) {
			Block cfg = Block.getBlockFromName(name);
			if (block == cfg)
				return false;
		}
		return true;
	}

	public static boolean isPlacable(Item item) {
		for (String name : CONFIG_TYPES.placeExclusion) {
			Block cfg = Block.getBlockFromName(name);
			if (((ItemBlock) item).getBlock() == cfg)
				return false;
		}
		return true;
	}
}
