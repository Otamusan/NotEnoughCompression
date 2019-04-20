package otamusan.nec.common;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import otamusan.nec.blocks.BlockCompressed;
import otamusan.nec.client.blockcompressed.TileSpecialItemRendererCompressed;
import otamusan.nec.common.automaticcompression.CompressionInChest;
import otamusan.nec.common.config.NECConfig;
import otamusan.nec.items.CompressedItemDiversity.ItemBlockCompressed;
import otamusan.nec.items.CompressedItemDiversity.ItemCompressed;
import otamusan.nec.recipes.CompressedCrafting;
import otamusan.nec.recipes.Compression;
import otamusan.nec.recipes.Decompression;
import otamusan.nec.recipes.DecompressionWithPiston;
import otamusan.nec.tileentity.TileCompressed;
import otamusan.nec.world.CompressedGenerator;

public class CommonProxy {
	public static IRecipe compression = new Compression();
	public static IRecipe uncompression = new Decompression();
	public static IRecipe uncompressionwithpiston = new DecompressionWithPiston();
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

		if (NECConfig.CONFIG_TYPES.isReplaceBlocks) {
			GameRegistry.registerWorldGenerator(new CompressedGenerator(), 1000);
		}

		ITEMBASE = new ItemCompressed();

		((Item) ITEMBASE).setRegistryName(Lib.MOD_ID + ":compresseditem_base");
		((Item) ITEMBASE).setUnlocalizedName("compresseditem");
		((Item) ITEMBASE).setTileEntityItemStackRenderer(TileSpecialItemRendererCompressed.instance);

		ForgeRegistries.ITEMS.register((Item) ITEMBASE);

		ITEMBLOCK = new ItemBlockCompressed();
		ITEMBASE.addChildren(ITEMBLOCK);

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
	public static ItemCompressed ITEMBASE;
	public static ItemBlockCompressed ITEMBLOCK;

	public void registerEventHandlers() {
		MinecraftForge.EVENT_BUS.register(new CompressionInChest());
	}
}