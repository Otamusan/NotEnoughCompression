package otamusan.nec.client.jei.CompressionRecipe;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import otamusan.nec.items.ItemCompressed;
import otamusan.nec.recipes.Compression;

public class CompressionRecipeWrapper implements IRecipeWrapper {
	private final List<List<ItemStack>> input;
	private final List<List<ItemStack>> output;

	public CompressionRecipeWrapper(Compression com) {
		input = new ArrayList<>();
		output = new ArrayList<>();

		output.add(getCompressedItemStacks(ForgeRegistries.ITEMS.getValues()));

		input.add(new ArrayList<ItemStack>());

		for (int i = 0; i < 9; i++) {
			if (i == 4) {
				input.add(filledPiston(ForgeRegistries.ITEMS.getValues()));
			} else {
				input.add(getItemStacks(ForgeRegistries.ITEMS.getValues()));
			}
		}
	}

	private List<ItemStack> getItemStacks(List<Item> items) {
		List<ItemStack> stacks = new ArrayList<>();
		for (int i = 0; i < items.size(); i++) {
			if (new ItemStack(items.get(i)).isEmpty())
				continue;
			stacks.add(new ItemStack(items.get(i)));
		}
		return stacks;
	}

	private List<ItemStack> getCompressedItemStacks(List<Item> items) {
		List<ItemStack> oriItemStacks = getItemStacks(items);
		List<ItemStack> stacks = new ArrayList<>();
		for (int i = 0; i < oriItemStacks.size(); i++) {
			stacks.add(ItemCompressed.createCompressedItem(oriItemStacks.get(i)));
		}
		return stacks;
	}

	private List<ItemStack> filledPiston(List<Item> items) {
		List<ItemStack> stacks = new ArrayList<>();
		for (int i = 0; i < items.size(); i++) {
			stacks.add(new ItemStack(Blocks.PISTON));
		}
		return stacks;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, input);
		ingredients.setOutputs(VanillaTypes.ITEM, output.get(0));
	}
}