package otamusan.nec.client.jei.CompressionRecipe;

import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.config.Constants;
import mezz.jei.util.Translator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import otamusan.nec.common.Lib;

public class CompressionRecipeCategory implements IRecipeCategory<IRecipeWrapper> {

	@Override
	public String getUid() {
		return Lib.MOD_ID + "_compressionrecipe";
	}

	private static final int craftOutputSlot = 0;
	private static final int craftInputSlot1 = 1;

	public static final int width = 116;
	public static final int height = 54;

	private final IDrawable background;
	private final IDrawable icon;
	private final String localizedName;
	private final ICraftingGridHelper craftingGridHelper;

	public CompressionRecipeCategory(IGuiHelper guiHelper) {
		ResourceLocation location = Constants.RECIPE_GUI_VANILLA;
		background = guiHelper.createDrawable(location, 0, 60, width, height);
		icon = guiHelper.createDrawableIngredient(new ItemStack(Blocks.CRAFTING_TABLE));
		localizedName = Translator.translateToLocal("gui.jei.category.craftingTable");
		craftingGridHelper = guiHelper.createCraftingGridHelper(craftInputSlot1, craftOutputSlot);
	}

	@Override
	public String getTitle() {
		return localizedName;
	}

	@Override
	public String getModName() {
		return Constants.MINECRAFT_NAME;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);
		guiItemStacks.init(craftOutputSlot, false, 94, 18);
		guiItemStacks.set(craftOutputSlot, outputs.get(0));

		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 3; ++x) {
				int index = craftInputSlot1 + x + (y * 3);
				guiItemStacks.init(index, true, x * 18, y * 18);
				guiItemStacks.set(index, inputs.get(index));
			}
		}
	}
}
