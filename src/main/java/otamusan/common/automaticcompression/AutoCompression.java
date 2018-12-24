package otamusan.common.automaticcompression;

import java.util.ArrayList;
import java.util.List;

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

	// 9回まで圧縮可
	public static List<ItemStack> fastautocompression(IInventory inv) {
		ArrayList<ItemStack> remains = new ArrayList<>();

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack itemStack = inv.getStackInSlot(i);
			if (itemStack.isEmpty())
				continue;

			ItemStack source;

			if (itemStack.getItem() instanceof ItemCompressed) {
				source = ItemCompressed.getOriginal(itemStack);
			} else {
				source = itemStack.copy();
			}

			int count = 0;
			for (int j = i; j < inv.getSizeInventory(); j++) {
				if (ItemStack.areItemStacksEqual(source, inv.getStackInSlot(j))) {
					count += inv.getStackInSlot(j).getCount();
					inv.setInventorySlotContents(j, ItemStack.EMPTY);
				} else if (inv.getStackInSlot(j).getItem() instanceof ItemCompressed) {
					ItemStack compressed = inv.getStackInSlot(j);
					if (ItemStack.areItemStacksEqual(ItemCompressed.getOriginal(compressed), source)
							&& ItemCompressed.getTime(compressed) < 10) {
						count += compressed.getCount() * Math.pow(8, ItemCompressed.getTime(compressed));
						inv.setInventorySlotContents(j, ItemStack.EMPTY);
					}
				}
			}

			List<ItemStack> compressed = getCompressedItemList(source, count);

			remains.addAll(InventoryUtil.putStacksInSlots(inv, compressed));

		}

		return remains;
	}

	// 9回まで
	public static List<ItemStack> getCompressedItemList(ItemStack source, int n) {
		ArrayList<ItemStack> items = new ArrayList<>();
		int buffer = n;
		for (int i = 0; i < getMaxTime(n) + 1; i++) {
			int time = getMaxTime(n) - i;
			int count = (int) Math.floor(buffer / Math.pow(8, time));
			ItemStack stack;
			if (time <= 0) {
				stack = source.copy();
			} else {
				stack = ItemCompressed.createCompressedItem(source, time);
			}
			stack.setCount(count);
			items.add(stack);
			buffer = OtamuModulo(buffer, (int) Math.pow(8, time));
		}
		return items;
	}

	private static int getMaxTime(int n) {
		return (int) Math.floor(Math.log(n) / Math.log(8));
	}

	private static int OtamuModulo(int a, int b) {
		if (a < b)
			return a;
		return a % b;
	}
}
