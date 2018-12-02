package otamusan.util;

import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;

public class InventoryUtil {
	public static void putStackInSlots(IInventory source, IInventory destination, ItemStack stack,
			EnumFacing direction) {
		TileEntityHopper.putStackInInventoryAllSlots(source, destination, stack, direction);
	}

	public static ItemStack putStackInSlots(IInventory destination, ItemStack stack) {
		return TileEntityHopper.putStackInInventoryAllSlots(null, destination, stack, null);
	}

	public static void putStacksInSlots(IInventory destination, List<ItemStack> list) {
		for (ItemStack itemStack : list) {
			TileEntityHopper.putStackInInventoryAllSlots(null, destination, itemStack, null);
		}
	}
}
