package otamusan.common.automaticcompression;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import otamusan.common.CommonProxy;
import otamusan.items.CompressedItems;
import otamusan.items.ItemCompressed;
import otamusan.util.InventoryUtil;

public class AutoCompression {
	public static List<ItemStack> autocompression2(IInventory inv) {
		List<ItemStack> remains = new ArrayList<>();
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack source;

			if (inv.getStackInSlot(i).getItem() == CommonProxy.itemCompressed) {
				source = ItemCompressed.getOriginal(inv.getStackInSlot(i));
			} else {
				source = inv.getStackInSlot(i).copy();
			}

			CompressedItems manager = new CompressedItems(source);
			if (source.isEmpty())
				continue;
			for (int j = i; j < inv.getSizeInventory(); j++) {
				boolean containable = manager.addCompressed(inv.getStackInSlot(j));
				if (containable) {
					inv.setInventorySlotContents(j, ItemStack.EMPTY);
				}
			}
			List<ItemStack> items = manager.getCompressed();
			remains.addAll(InventoryUtil.putStacksInSlots(inv, items));
		}
		return remains;
	}
}
