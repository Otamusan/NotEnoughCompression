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

		public static Compression compression = new Compression();
		public static JEI jei = new JEI();
		public static Using using = new Using();
		public static World world = new World();

		public static class World {
			@Comment({ "Whether to replace generated chunks in the world" })
			public boolean isReplaceChunks = true;

			@Comment({ "Chunk replacement rate" })
			public double rate = 0.005D;

			@Comment({ "Maximum compressed time of blocks in chunks replaced" })
			public int maxTimeReplaced = 5;

			@Comment({ "Deviation of the replaced compressed time" })
			public int deviationofTime = 6;
		}

		public static class Using {
			@Comment({ "Blocks written here can not be placed even if it is compressed" })
			public String[] placeExclusion = {};

			@Comment({ "Restriction of time of compressed Item on using" })
			public int usingCompressionTime = 3;

			@Comment({ "Items written here can not use" })
			public String[] usingExclusion = {};

			@Comment({ "Whether to update all compressed item in inventory" })
			public boolean isUpdateOnlyOnHand = false;
		}

		public static class Compression {
			@Comment({ "Items written here can not be compressed" })
			public String[] compressedExclusion = {};

			@Comment({ "Catalyst of compression" })
			public String compressionCatalyst = "minecraft:piston";

			@Comment({ "Catalyst of decompression" })
			public String decompressionCatalyst = "minecraft:sticky_piston";
		}

		public static class JEI {
			@Comment({ "Whether to show the recipe of compression" })
			public boolean JEIshowCompression = true;

			@Comment({ "Whether to show the recipe of uncompression" })
			public boolean JEIshowUncompression = true;

			@Comment({ "Whether to show the recipe of compressed crating" })
			public boolean JEIshowCompressedCraft = true;

			@Comment({ "Number of compression recipes to be displayed on JEI" })
			public int JEIshowCompressionTime = 5;
		}
	}

	public static Item getCompressionCatalyst() {
		return Item.getByNameOrId(CONFIG_TYPES.compression.compressionCatalyst);
	}

	public static Item getDecompressionCatalyst() {
		return Item.getByNameOrId(CONFIG_TYPES.compression.decompressionCatalyst);
	}

	public static boolean isCompressionCatalyst(Item item) {
		Item cfgitem = Item.getByNameOrId(CONFIG_TYPES.compression.compressionCatalyst);
		return item == cfgitem;
	}

	public static boolean isDecompressionCatalyst(Item item) {
		Item cfgitem = Item.getByNameOrId(CONFIG_TYPES.compression.decompressionCatalyst);
		return item == cfgitem;
	}

	public static boolean isCompressible(Item item) {
		for (String name : CONFIG_TYPES.compression.compressedExclusion) {
			Item cfgItem = Item.getByNameOrId(name);
			if (item == cfgItem)
				return false;
		}
		return true;
	}

	public static boolean isUsable(Item item) {
		for (String name : CONFIG_TYPES.compression.compressedExclusion) {
			Item cfgItem = Item.getByNameOrId(name);
			if (item == cfgItem)
				return false;
		}
		return true;
	}

	public static boolean isPlacable(Block block) {
		for (String name : CONFIG_TYPES.using.placeExclusion) {
			Block cfg = Block.getBlockFromName(name);
			if (block == cfg)
				return false;
		}
		return true;
	}

	public static boolean isPlacable(Item item) {
		for (String name : CONFIG_TYPES.using.placeExclusion) {
			Block cfg = Block.getBlockFromName(name);
			if (((ItemBlock) item).getBlock() == cfg)
				return false;
		}
		return true;
	}
}
