package otamusan.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import otamusan.NotEnoughCompression;
import otamusan.blocks.BlockCompressed;
import otamusan.common.automaticcompression.CompressionInChest;
import otamusan.recipes.CompressedCrafting;
import otamusan.recipes.Compression;
import otamusan.recipes.Uncompression;
import otamusan.tileentity.TileCompressed;

public class CommonProxy {

	public static final ResourceLocation RBlockCompressed = new ResourceLocation(
			NotEnoughCompression.MOD_ID + ":blockcompressed", "inventory");
	public static final ResourceLocation RItemCompressed = new ResourceLocation(
			NotEnoughCompression.MOD_ID + ":itemcompressed", "inventory");

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

		blockCompressed = (BlockCompressed) (new BlockCompressed().setUnlocalizedName("compressedblock"));
		blockCompressed.setRegistryName("compressedblock");
		ForgeRegistries.BLOCKS.register(blockCompressed);

		itemBlockCompressed = new ItemBlock(blockCompressed);
		itemBlockCompressed.setRegistryName(blockCompressed.getRegistryName());
		ForgeRegistries.ITEMS.register(itemBlockCompressed);

		NECItems.itemcompressed.setRegistryName("compresseditem");
		ForgeRegistries.ITEMS.register(NECItems.itemcompressed);
		NECItems.itemcompressed.setUnlocalizedName("compresseditem");
		NECItems.itemcompressed.setCreativeTab(CreativeTabs.MISC);

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
	public static ItemBlock itemBlockCompressed;

	public void registerEventHandlers() {
		MinecraftForge.EVENT_BUS.register(new CompressionInChest());
	}
}