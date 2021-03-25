package otamusan.nec.recipes;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import otamusan.nec.common.Lib;

import java.util.ArrayList;

public class RecipeReplacer {

	public static ArrayList<IRecipe> existingRecipe = new ArrayList<>();

	public static void replaceAll() {
		for (IRecipe recipe : ForgeRegistries.RECIPES.getValues()) {
			if (isReplaceable(recipe)) {
				IRecipe hard = new HeavyCrafting(recipe);
				hard.setRegistryName(recipe.getRegistryName());
				existingRecipe.add(recipe);
				ForgeRegistries.RECIPES.register(hard);
			}
		}
	}

	private static boolean isReplaceable(IRecipe iRecipe) {
		return iRecipe.getClass() == ShapedRecipes.class || iRecipe.getClass() == ShapelessRecipes.class
				|| iRecipe.getClass() == ShapedOreRecipe.class || iRecipe.getClass() == ShapelessOreRecipe.class;
	}

	public static void replaceNECRecipe() {
		IRecipe recipe = new CompressedHeavyCrafting();
		recipe.setRegistryName(new ResourceLocation(Lib.MOD_ID, "compressedcrafting"));
		ForgeRegistries.RECIPES.register(recipe);
	}
}