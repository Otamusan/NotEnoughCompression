package otamusan.nec.common.config;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
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
		for (String name : CONFIG_TYPES.compressedExclusion) {
			Block cfg = Block.getBlockFromName(name);
			if (block == cfg)
				return false;
		}
		return true;
	}
}
