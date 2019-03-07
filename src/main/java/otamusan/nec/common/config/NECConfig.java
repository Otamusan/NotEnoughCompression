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
		@Comment({ "Blocks written here can not be placed even if it is compressed" })
		public static String[] placeExclusion = {};
		@Comment({ "Number of compression recipes to be displayed on JEI" })
		public static int JEIshowCompressionTime = 5;
		@Comment({ "Whether to show the recipe of compression" })
		public static boolean JEIshowCompression = true;
		@Comment({ "Whether to show the recipe of uncompression" })
		public static boolean JEIshowUncompression = true;
		@Comment({ "Whether to show the recipe of compressed crating" })
		public static boolean JEIshowCompressedCraft = true;

	}

	public static boolean isCompressible(Item item) {
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
