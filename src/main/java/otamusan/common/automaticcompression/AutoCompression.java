package otamusan.common.automaticcompression;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import otamusan.items.ItemCompressed;
import otamusan.util.InventoryUtil;

public class AutoCompression {
	public static void autocompression(IInventory inv) {
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.getCount() >= 8) {
				ItemStack compressed = ItemCompressed.createCompressedItem(stack);
				if (InventoryUtil.putStackInSlots(inv, compressed).isEmpty()) {
					stack.setCount(stack.getCount() - 8);
				}
			}
		}
	}
}
