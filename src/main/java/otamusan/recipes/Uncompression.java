package otamusan.recipes;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import otamusan.common.NECItems;
import otamusan.items.ItemCompressed;

public class Uncompression extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		ItemStack base = getBase(inv);
		return getItemAmount(inv) == 1 && base != null;
	}

	private int getItemAmount(InventoryCrafting inv) {
		int a = 0;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (inv.getStackInSlot(i).getItem() != Items.AIR) {
				a++;
			}
		}
		return a;
	}

	private ItemStack getBase(InventoryCrafting inv) {
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack item = inv.getStackInSlot(i);
			if (item.getItem() != Items.AIR) {
				if (item.getItem() == NECItems.itemcompressed) {
					return item.copy();
				} else {
					return null;
				}
			}
		}
		return null;
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(Items.NETHER_STAR);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return ItemCompressed.createUncompressedItem(getBase(inv));
	}
}
