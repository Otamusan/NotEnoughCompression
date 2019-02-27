package otamusan.nec.items;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public class CompressedItems {

	public int[] counts;
	private ItemStack original;

	public CompressedItems(ItemStack ori) {
		this.counts = new int[50];
		this.original = ori.copy();
	}

	public int getCount(int time) {
		return counts[time];
	}

	public void setCount(int time, int count) {
		counts[time] = count;
	}

	public boolean isContainable(ItemStack stack) {
		if (ItemStack.areItemStacksEqual(stack, original))
			return true;
		return ItemStack.areItemStacksEqual(ItemCompressed.getOriginal(stack), original);
	}

	public boolean addCompressed(ItemStack stack) {
		if (!isContainable(stack))
			return false;
		int time = ItemCompressed.getTime(stack);
		counts[time] = getCount(time) + stack.getCount();
		countsUpdate();
		return true;
	}

	private void countsUpdate() {
		for (int i = 0; i < counts.length; i++) {
			if (getCount(i) >= 8) {
				int n = getCount(i);
				setCount(i, OtamuModulo(n, 8));
				setCount(i + 1, n / 8);
			}
		}
	}

	public ArrayList<ItemStack> getCompressed(int time) {
		ArrayList<ItemStack> items = new ArrayList<>();
		int count = getCount(time);
		ItemStack item = CompressedItems.createCompressedItem(original.copy(), time);

		int stackn = count / item.getMaxStackSize();
		int n = OtamuModulo(count, item.getMaxStackSize());
		for (int i = 0; i < stackn; i++) {

			ItemStack bitem = item.copy();
			bitem.setCount(item.getMaxStackSize());
			items.add(bitem);
		}
		ItemStack bitem = CompressedItems.createCompressedItem(item.copy(), time);
		if (n == 0)
			return items;
		bitem.setCount(n);
		items.add(bitem);

		return items;
	}

	public ArrayList<ItemStack> getCompressed() {
		ArrayList<ItemStack> items = new ArrayList<>();
		for (int i = 0; i < counts.length; i++) {
			items.addAll(getCompressed(i));
		}
		return items;
	}

	private static int OtamuModulo(int a, int b) {
		if (a < b)
			return a;
		return a % b;
	}

	// timeが0の時も考慮
	public static ItemStack createCompressedItem(ItemStack uncompressed, int time) {
		if (time == 0) {
			ItemStack item = uncompressed.copy();
			item.setCount(1);
			return item;
		} else {
			return ItemCompressed.createCompressedItem(uncompressed, time);
		}
	}
}