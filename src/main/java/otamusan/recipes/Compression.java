package otamusan.recipes;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import otamusan.common.CommonProxy;
import otamusan.items.ItemCompressed;

public class Compression extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	final ItemStack catalyst = new ItemStack(Blocks.PISTON);

	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		ItemStack base = ItemStack.EMPTY;
		int baseamount = 0;
		boolean isCatalystPresented = false;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (base == ItemStack.EMPTY && !isCatalyst(inv.getStackInSlot(i))) {
				base = inv.getStackInSlot(i).copy();
				baseamount++;
			} else if (isCatalyst(inv.getStackInSlot(i)) && inv.getStackInSlot(i).getCount() == 1) {
				isCatalystPresented = true;
			} else if (base.isItemEqual(inv.getStackInSlot(i)) && !isCatalyst(inv.getStackInSlot(i))) {
				baseamount++;
			}
		}

		// 2147483647回以上の圧縮を制限
		if (base.getItem()==CommonProxy.itemCompressed) {
			int time = ItemCompressed.getTime(base);
			if (time>=Integer.MAX_VALUE-1)
				return false;
		}

		return base != ItemStack.EMPTY && baseamount == 8 && isCatalystPresented;
	}

	private boolean isCatalyst(ItemStack item) {
		if (!item.isItemEqual(catalyst))
			return false;
		return true;
	}

	private ItemStack getBase(InventoryCrafting inv) {
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (!isCatalyst(inv.getStackInSlot(i))) {
				return inv.getStackInSlot(i).copy();
			}
		}
		return null;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		NonNullList<ItemStack> list = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (isCatalyst(inv.getStackInSlot(i))) {
				list.set(i, new ItemStack(Blocks.PISTON));
			} else {
				list.set(i, ItemStack.EMPTY);
			}
		}
		return list;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack compressed = this.getBase(inv);
		return ItemCompressed.createCompressedItem(compressed);
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(Blocks.PISTON);
	}
}
