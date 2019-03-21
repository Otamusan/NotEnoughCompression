package otamusan.nec.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import otamusan.nec.blocks.BlockCompressed;
import otamusan.nec.common.automaticcompression.CompressionInChest;
import otamusan.nec.common.config.NECConfig;
import otamusan.nec.items.ItemCompressed;
import otamusan.nec.recipes.CompressedCrafting;
import otamusan.nec.recipes.Compression;
import otamusan.nec.recipes.Uncompression;
import otamusan.nec.recipes.UncompressionWithPiston;
import otamusan.nec.tileentity.TileCompressed;
import otamusan.nec.world.CompressedGenerator;

public class CommonProxy {
	public static IRecipe compression = new Compression();
	public static IRecipe uncompression = new Uncompression();
	public static IRecipe uncompressionwithpiston = new UncompressionWithPiston();
	public static IRecipe compressedcrafting = new CompressedCrafting();

	public static void addShapedRecipe(ItemStack item, Object... param) {

		ForgeRegistries.RECIPES
				.register(new ShapedOreRecipe(new ResourceLocation(Lib.MOD_ID, getName(item, param)), item, param)
						.setRegistryName(Lib.MOD_ID, getName(item, param)));
	}

	public static String getName(Object ob, Object... param) {
		String name = ob.toString() + param.toString();
		return name;
	}

	public void preInit() {
		compressedcrafting.setRegistryName(new ResourceLocation(Lib.MOD_ID, "compressedcrafting"));
		ForgeRegistries.RECIPES.register(compressedcrafting);

		compression.setRegistryName(new ResourceLocation(Lib.MOD_ID, "compression"));
		ForgeRegistries.RECIPES.register(compression);

		uncompression.setRegistryName(new ResourceLocation(Lib.MOD_ID, "uncompression"));
		ForgeRegistries.RECIPES.register(uncompression);

		uncompressionwithpiston.setRegistryName(new ResourceLocation(Lib.MOD_ID, "uncompressionwithpiston"));
		ForgeRegistries.RECIPES.register(uncompressionwithpiston);

		blockCompressed = new BlockCompressed();
		blockCompressed.setRegistryName("compressedblock");
		blockCompressed.setUnlocalizedName("compressedblock");
		ForgeRegistries.BLOCKS.register(blockCompressed);

		itemCompressed = new ItemCompressed(blockCompressed);
		itemCompressed.setRegistryName("compresseditem");
		itemCompressed.setUnlocalizedName("compresseditem");
		itemCompressed.setCreativeTab(CreativeTabs.MISC);
		ForgeRegistries.ITEMS.register(itemCompressed);
		if (NECConfig.CONFIG_TYPES.isReplaceBlocks) {
			GameRegistry.registerWorldGenerator(new CompressedGenerator(), 1000);
		}

	}

	public void init() {

	}

	public void postInit() {
	}

	public World getClientWorld() {
		return null;
	}

	public void registerTileEntity() {
		GameRegistry.registerTileEntity(TileCompressed.class, new ResourceLocation(Lib.MOD_ID, "tilecompressed"));
	}

	public static BlockCompressed blockCompressed;
	public static ItemCompressed itemCompressed;

	public void registerEventHandlers() {
		MinecraftForge.EVENT_BUS.register(new CompressionInChest());
	}
}