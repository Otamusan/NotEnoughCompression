package otamusan.register;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import otamusan.NotEnoughCompression;
import otamusan.items.ItemCompressed;

public class ItemRegister {
	public static Side Fside;
	public static final Item itemcompressed = new ItemCompressed();

	public static void init(Side side) {
		register(itemcompressed, "itemcompressed", "inventory");
	}

	public static void register(Item item, String str, String varient) {
		register(item, str);
		modelRegister(item, str + "#" + varient);
	}

	public static void register(Item item, String str) {
		item.setRegistryName(new ResourceLocation(NotEnoughCompression.MOD_ID + ":" + str));
		ForgeRegistries.ITEMS.register(item);
		if (Fside == Side.CLIENT) {
			modelRegister(item, str);
		}
		item.setUnlocalizedName(str);
		item.setCreativeTab(CreativeTabs.MISC);
	}

	@SideOnly(Side.CLIENT)
	public static void modelRegister(Item item, String str) {
		ModelLoader.setCustomModelResourceLocation(item, 0,
				new ModelResourceLocation(NotEnoughCompression.MOD_ID + ":" + str));
	}
}
