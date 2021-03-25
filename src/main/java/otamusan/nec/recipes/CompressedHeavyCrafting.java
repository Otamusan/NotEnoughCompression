package otamusan.nec.recipes;

import java.util.Collection;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import otamusan.nec.items.CompressedItemDiversity.ItemCompressed;

public class CompressedHeavyCrafting extends CompressedCrafting {
	@Override
	public Collection<IRecipe> getExistingRecipes() {
		return RecipeReplacer.existingRecipe;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		IRecipe recipe = findMatchingRecipe(getExistingRecipes(), getUncompresserdInv(inv), world);
		ItemStack created = ItemCompressed.createCompressedItem(recipe.getCraftingResult(getUncompresserdInv(inv)),
				compressedTime(inv) - 1);
		created.setCount(recipe.getCraftingResult(getUncompresserdInv(inv)).getCount());
		return created;
	}

}