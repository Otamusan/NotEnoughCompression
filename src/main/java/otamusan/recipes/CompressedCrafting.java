package otamusan.recipes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistryEntry;
import otamusan.common.CommonProxy;
import otamusan.items.ItemCompressed;

public class CompressedCrafting extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	private World world;

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		this.world = worldIn;

		if (compressedTime(inv) == 0)
			return false;

		IRecipe recipe = CraftingManager.findMatchingRecipe(getUncompresserdInv(inv), worldIn);

		return recipe instanceof ShapedRecipes || recipe instanceof ShapelessRecipes
				|| recipe instanceof ShapedOreRecipe || recipe instanceof ShapelessRecipes;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		InventoryCrafting ucInv = getUncompresserdInv(inv);
		IRecipe recipe = CraftingManager.findMatchingRecipe(ucInv, world);

		NonNullList<ItemStack> reInv = recipe.getRemainingItems(ucInv);

		NonNullList<ItemStack> output = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

		for (int i = 0; i < reInv.size(); i++) {
			if (reInv.get(i).getItem() == Items.AIR) {
				output.set(i, ItemStack.EMPTY);
			} else {
				output.set(i, ItemCompressed.createCompressedItem(reInv.get(i), compressedTime(inv)));
			}

		}

		return output;
	}

	private int compressedTime(InventoryCrafting inv) {
		int time = 0;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (
				inv.getStackInSlot(i).getItem()!=CommonProxy.itemCompressed
					&& inv.getStackInSlot(i).getItem() != Items.AIR)
				return 0;

			if (inv.getStackInSlot(i).getItem() == Items.AIR)
				continue;

			if (time == 0) {
				time = ItemCompressed.getTime(inv.getStackInSlot(i));
			} else if (time != ItemCompressed.getTime(inv.getStackInSlot(i))) {
				return 0;
			}
		}
		return time;
	}

	private InventoryCrafting ICCopy(InventoryCrafting inv) {
		InventoryCrafting newinv = new InventoryCrafting(new Container() {
			@Override
			public boolean canInteractWith(EntityPlayer playerIn) {
				return false;
			}
		}, inv.getWidth(), inv.getHeight());

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			newinv.setInventorySlotContents(i, inv.getStackInSlot(i).copy());
		}
		return newinv;
	}

	private InventoryCrafting getUncompresserdInv(InventoryCrafting inv) {
		InventoryCrafting after = ICCopy(inv);
		InventoryCrafting newinv = ICCopy(inv);
		for (int i = 0; i < newinv.getSizeInventory(); i++) {
			ItemStack slot = newinv.getStackInSlot(i).copy();
			ItemStack original;

			if (slot.getItem() == Items.AIR) {
				original = slot;
			} else {
				original = ItemCompressed.getOriginal(slot);
			}

			after.setInventorySlotContents(i, original);
		}
		return after;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		IRecipe recipe = CraftingManager.findMatchingRecipe(getUncompresserdInv(inv), world);
		ItemStack created = ItemCompressed.createCompressedItem(recipe.getCraftingResult(getUncompresserdInv(inv)),
				compressedTime(inv));
		created.setCount(recipe.getCraftingResult(getUncompresserdInv(inv)).getCount());
		return created;
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(CommonProxy.itemCompressed);
	}

}
