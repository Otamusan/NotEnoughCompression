package otamusan.nec.recipes;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import otamusan.nec.common.CommonProxy;
import otamusan.nec.common.config.NECConfig;
import otamusan.nec.items.CompressedItemDiversity.ItemCompressed;

public class DecompressionWithPiston extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	public static final ItemStack catalyst = new ItemStack(Item.getItemFromBlock(Blocks.STICKY_PISTON));

	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		return getItemAmount(inv) == 2 && hasCatalyst(inv) && !getBase(inv).isEmpty();
	}

	private ItemStack getBase(InventoryCrafting inv) {
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack current = inv.getStackInSlot(i);
			if (!current.isEmpty()) {
				if (ItemCompressed.isCompressedItem(current.getItem()))
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

	private boolean hasCatalyst(InventoryCrafting inv) {
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (isCatalyst(inv.getStackInSlot(i))) {
				return true;
			}
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
			if (isCatalyst(inv.getStackInSlot(i))) {
				list.set(i, inv.getStackInSlot(i).copy());
			}
		}
		return list;
	}

	private boolean isCatalyst(ItemStack item) {
		return NECConfig.isDecompressionCatalyst(item.getItem());
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(CommonProxy.ITEMBASE);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return ItemCompressed.createUncompressedItem(getBase(inv));
	}

	@Override
	public boolean isDynamic() {
		return true;
	}
}
