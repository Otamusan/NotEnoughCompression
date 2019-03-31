package otamusan.nec.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class InventoryUtil {
	public static void putStackInSlots(IInventory source, IInventory destination, ItemStack stack,
			EnumFacing direction) {
		TileEntityHopper.putStackInInventoryAllSlots(source, destination, stack, direction);
	}

	public static ItemStack putStackInSlots(IInventory destination, ItemStack stack) {
		return TileEntityHopper.putStackInInventoryAllSlots(null, destination, stack, null);
	}

	public static List<ItemStack> putStacksInSlots(IInventory destination, List<ItemStack> list) {
		ArrayList<ItemStack> remain = new ArrayList<>();
		for (ItemStack itemStack : list) {
			remain.add(TileEntityHopper.putStackInInventoryAllSlots(null, destination, itemStack, null));
		}
		return remain;
	}

	public static boolean canCombine(ItemStack stack1, ItemStack stack2) {
		if (stack1.getItem() != stack2.getItem()) {
			return false;
		} else if (stack1.getMetadata() != stack2.getMetadata()) {
			return false;
		} else if (stack1.getCount() > stack1.getMaxStackSize()) {
			return false;
		} else {
			return ItemStack.areItemStackTagsEqual(stack1, stack2);
		}
	}

	public static IItemHandler getItemHandler(ArrayList<ItemStack> stacks) {
		ItemStackHandler handler = new ItemStackHandler(stacks.size());
		for (ItemStack itemStack : stacks) {
			handler.insertItem(0, itemStack.copy(), false);
		}
		return handler;
	}

	public static ArrayList<ItemStack> getItemList(IItemHandler stacks) {
		ArrayList<ItemStack> newstacks = new ArrayList<>();
		for (int i = 0; i < stacks.getSlots(); i++) {
			if (!stacks.getStackInSlot(i).isEmpty()) {
				newstacks.add(stacks.getStackInSlot(i));
			}
		}
		return newstacks;
	}
}
