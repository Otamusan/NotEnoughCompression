package otamusan.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import otamusan.NotEnoughCompression;
import otamusan.blocks.BlockCompressed;
import otamusan.common.automaticcompression.CompressionInChest;
import otamusan.items.ItemCompressed;
import otamusan.recipes.CompressedCrafting;
import otamusan.recipes.Compression;
import otamusan.recipes.Uncompression;
import otamusan.tileentity.TileCompressed;

public class CommonProxy {

	public void preInit() {
		IRecipe compression = new Compression();
		compression.setRegistryName(new ResourceLocation(NotEnoughCompression.MOD_ID, "compression"));
		ForgeRegistries.RECIPES.register(compression);

		IRecipe uncompression = new Uncompression();
		uncompression.setRegistryName(new ResourceLocation(NotEnoughCompression.MOD_ID, "uncompression"));
		ForgeRegistries.RECIPES.register(uncompression);

		IRecipe compressedcrafting = new CompressedCrafting();
		compressedcrafting.setRegistryName(new ResourceLocation(NotEnoughCompression.MOD_ID, "compressedcrafting"));
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
		GameRegistry.registerTileEntity(TileCompressed.class,
				new ResourceLocation(NotEnoughCompression.MOD_ID, "tilecompressed"));
	}

	public static BlockCompressed blockCompressed;
	public static ItemCompressed itemCompressed;

	public void registerEventHandlers() {
		MinecraftForge.EVENT_BUS.register(new CompressionInChest());
	}
}