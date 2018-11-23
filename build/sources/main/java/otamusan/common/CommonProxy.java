package otamusan.common;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import otamusan.NotEnoughCompression;
import otamusan.recipes.CompressedCrafting;
import otamusan.recipes.Compression;
import otamusan.recipes.Uncompression;

public class CommonProxy {
	public World getClientWorld() {
		return null;
	}

	public void registerTileEntity() {

	}

	public void registerEventHandlers() {
		IRecipe compression = new Compression();
		compression.setRegistryName(new ResourceLocation(NotEnoughCompression.MOD_ID, "compression"));
		ForgeRegistries.RECIPES.register(compression);

		IRecipe uncompression = new Uncompression();
		uncompression.setRegistryName(new ResourceLocation(NotEnoughCompression.MOD_ID, "uncompression"));
		ForgeRegistries.RECIPES.register(uncompression);

		IRecipe compressedcrafting = new CompressedCrafting();
		compressedcrafting.setRegistryName(new ResourceLocation(NotEnoughCompression.MOD_ID, "compressedcrafting"));
		ForgeRegistries.RECIPES.register(compressedcrafting);
	}
}