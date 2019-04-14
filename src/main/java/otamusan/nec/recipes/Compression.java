package otamusan.nec.recipes;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import otamusan.nec.common.config.NECConfig;
import otamusan.nec.items.CompressedItemDiversity.ItemCompressed;

public class Compression extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		ItemStack base = ItemStack.EMPTY;
		int baseamount = 0;
		boolean isCatalystPresented = false;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack current = inv.getStackInSlot(i);
			if (isCatalyst(current)) {
				if (current.getCount() == 1)
					isCatalystPresented = true;
			} else if (!current.isEmpty()) {
				if (base.isEmpty()) {
					base = current.copy();
					baseamount++;
				} else if (ItemCompressed.isCompressedItemEqual(base, current)) {
					baseamount++;
				}
			}
		}

		// 2147483647回以上の圧縮を制限
		int time = ItemCompressed.getTime(base);
		if (time >= Integer.MAX_VALUE - 1)
			return false;
		if (!NECConfig.isCompressible(base.getItem()))
			return false;

		return !base.isEmpty() && baseamount == 8 && isCatalystPresented;
	}

	private boolean isCatalyst(ItemStack item) {
		return NECConfig.isCompressionCatalyst(item.getItem());
	}

	private ItemStack getBase(InventoryCrafting inv) {
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (!isCatalyst(inv.getStackInSlot(i))) {
				return inv.getStackInSlot(i).copy();
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		NonNullList<ItemStack> list = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (isCatalyst(inv.getStackInSlot(i)))
				list.set(i, inv.getStackInSlot(i).copy());
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

	@Override
	public boolean isDynamic() {
		return true;
	}
}
