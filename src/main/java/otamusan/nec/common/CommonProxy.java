package otamusan.nec.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import otamusan.nec.blocks.BlockCompressed;
import otamusan.nec.common.automaticcompression.CompressionInChest;
import otamusan.nec.items.ItemCompressed;
import otamusan.nec.recipes.CompressedCrafting;
import otamusan.nec.recipes.Compression;
import otamusan.nec.recipes.Uncompression;
import otamusan.nec.tileentity.TileCompressed;

public class CommonProxy {
	public static IRecipe compression = new Compression();
	public static IRecipe uncompression = new Uncompression();
	public static IRecipe compressedcrafting = new CompressedCrafting();

	public void preInit() {
		compression.setRegistryName(new ResourceLocation(Lib.MOD_ID, "compression"));
		ForgeRegistries.RECIPES.register(compression);

		uncompression.setRegistryName(new ResourceLocation(Lib.MOD_ID, "uncompression"));
		ForgeRegistries.RECIPES.register(uncompression);

		compressedcrafting.setRegistryName(new ResourceLocation(Lib.MOD_ID, "compressedcrafting"));
		ForgeRegistries.RECIPES.register(compressedcrafting);

		blockCompressed = new BlockCompressed();
		blockCompressed.setRegistryName("compressedblock");
		blockCompressed.setUnlocalizedName("compressedblock");
		ForgeRegistries.BLOCKS.register(blockCompressed);

		itemCompressed = new ItemCompressed(blockCompressed);
		itemCompressed.setRegistryName("compresseditem");
		itemCompressed.setUnlocalizedName("compresseditem");
		itemCompressed.setCreativeTab(CreativeTabs.MISC);
		ForgeRegistries.ITEMS.register(itemCompressed);
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