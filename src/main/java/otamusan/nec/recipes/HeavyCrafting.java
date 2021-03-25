package otamusan.nec.recipes;

import java.util.Collection;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import otamusan.nec.items.CompressedItemDiversity.IItemCompressed;
import otamusan.nec.items.CompressedItemDiversity.ItemCompressed;

public class HeavyCrafting extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
	private IRecipe original;

	public HeavyCrafting(IRecipe recipe) {
		original = recipe;
	}

	public IRecipe findMatchingRecipe(Collection<IRecipe> recipes, InventoryCrafting craftMatrix, World worldIn) {
		return original;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack created = ItemCompressed.createCompressedItem(original.getCraftingResult(getUncompresserdInv(inv)),
				compressedTime(inv) - 1);
		created.setCount(original.getCraftingResult(getUncompresserdInv(inv)).getCount());
		return created;
	}

	protected World world;

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		this.world = worldIn;

		if (compressedTime(inv) != 1)
			return false;

		InventoryCrafting uncompinv = getUncompresserdInv(inv);
		return original.matches(uncompinv, worldIn);

	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		InventoryCrafting ucInv = getUncompresserdInv(inv);
		NonNullList<ItemStack> reInv = original.getRemainingItems(ucInv);

		NonNullList<ItemStack> output = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

		for (int i = 0; i < reInv.size(); i++) {
			if (!reInv.get(i).isEmpty())
				output.set(i, ItemCompressed.createCompressedItem(reInv.get(i), compressedTime(inv)));
		}

		return output;
	}

	protected int compressedTime(InventoryCrafting inv) {
		int time = 0;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack current = inv.getStackInSlot(i);
			if (current.isEmpty())
				continue;

			if (current.getItem() instanceof IItemCompressed)
				return 0;

			if (time == 0)
				time = ItemCompressed.getTime(current);
			else if (time != ItemCompressed.getTime(current))
				return 0;
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

	protected InventoryCrafting getUncompresserdInv(InventoryCrafting inv) {
		InventoryCrafting after = ICCopy(inv);
		InventoryCrafting newinv = ICCopy(inv);
		for (int i = 0; i < newinv.getSizeInventory(); i++) {
			ItemStack slot = newinv.getStackInSlot(i).copy();
			ItemStack original;

			if (slot.isEmpty()) {
				original = slot;
			} else {
				original = ItemCompressed.getOriginal(slot);
			}

			after.setInventorySlotContents(i, original);
		}
		return after;
	}

	@Override
	public boolean canFit(int width, int height) {
		return original.canFit(width, height);
	}

	@Override
	public ItemStack getRecipeOutput() {
		return original.getRecipeOutput();
	}
}