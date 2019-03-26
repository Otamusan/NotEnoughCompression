package otamusan.nec.recipes;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import otamusan.nec.common.CommonProxy;
import otamusan.nec.items.ItemCompressed;

public class UncompressionWithPiston extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		return getItemAmount(inv) == 2 && hasPiston(inv) && !getBase(inv).isEmpty();
	}

	private ItemStack getBase(InventoryCrafting inv) {
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack current = inv.getStackInSlot(i);
			if (!current.isEmpty()) {
				if (current.getItem() == CommonProxy.itemCompressed)
					return current.copy();
			}
		}
		return ItemStack.EMPTY;
	}

	private int getItemAmount(InventoryCrafting inv) {
		int a = 0;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (!inv.getStackInSlot(i).isEmpty())
				a++;
		}
		return a;
	}

	private boolean hasPiston(InventoryCrafting inv) {
		for (int i = 0; i < inv.getSizeInventory(); i++) {

			if (!(inv.getStackInSlot(i).getItem() instanceof ItemBlock))
				continue;
			Block block = ((ItemBlock) inv.getStackInSlot(i).getItem()).getBlock();
			if (block == Blocks.STICKY_PISTON)
				return true;
		}
		return false;
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		NonNullList<ItemStack> list = NonNullList.withSize(9, ItemStack.EMPTY);
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (inv.getStackInSlot(i).getItem() instanceof ItemBlock
					&& ((ItemBlock) inv.getStackInSlot(i).getItem()).getBlock() == Blocks.STICKY_PISTON) {
				list.set(i, inv.getStackInSlot(i));
			}
		}
		return list;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(CommonProxy.itemCompressed);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return ItemCompressed.createUncompressedItem(getBase(inv));
	}
}
