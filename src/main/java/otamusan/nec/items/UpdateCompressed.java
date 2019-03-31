package otamusan.nec.items;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import otamusan.nec.common.config.NECConfig;
import otamusan.nec.items.UsingCompressed.Remains;
import otamusan.nec.util.InventoryUtil;

public class UpdateCompressed {

	public static void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

		if (!(entityIn instanceof EntityPlayer))
			return;

		EntityPlayer player = (EntityPlayer) entityIn;

		ItemStack compressed = getItem(player, itemSlot);
		ItemStack original = ItemCompressed.getOriginal(compressed);

		if (ItemCompressed.getTime(compressed) >= NECConfig.CONFIG_TYPES.UsingCompressionTime)
			return;

		if (!NECConfig.isUsable(original.getItem()))
			return;

		long n = (long) Math.pow(8, ItemCompressed.getTime(compressed));

		ArrayList<Remains> remainss = new ArrayList<>();

		for (int j = 0; j < compressed.getCount(); j++) {
			setItem(player, itemSlot, ItemStack.EMPTY);

			Remains remains = new Remains();

			for (int i = 0; i < n; i++) {
				ItemStack use = original.copy();
				use.setCount(1);
				setItem(player, itemSlot, use);

				use.getItem().onUpdate(use, worldIn, entityIn, itemSlot, isSelected);

				ItemStack remain = getItem(player, itemSlot);

				if (!remain.isEmpty()) {
					remains.addItem(remain.copy());
				}
				setItem(player, itemSlot, ItemStack.EMPTY);
			}

			remainss.add(remains);
		}

		ItemStackHandler remainstack = new ItemStackHandler(100);

		for (Remains remains : remainss) {
			marge(remainstack, InventoryUtil.getItemHandler(remains.getCompresseds()));
		}

		System.out.println(InventoryUtil.getItemList(remainstack));

		if (InventoryUtil.getItemList(remainstack).size() == 1
				&& ItemStack.areItemStacksEqual(compressed, remainstack.getStackInSlot(0))) {
			setItem(player, itemSlot, compressed);
			System.out.println(434);
		} else {
			UsingCompressed.putRemain(player, InventoryUtil.getItemList(remainstack));
		}
	}

	public static void marge(IItemHandler newitems, IItemHandler stacks) {
		for (int i = 0; i < stacks.getSlots(); i++) {
			newitems.insertItem(0, stacks.getStackInSlot(i), false);
		}
	}

	public static ItemStack getItem(EntityPlayer player, int i) {
		return player.inventory.getStackInSlot(i);
	}

	public static void setItem(EntityPlayer player, int i, ItemStack itemStack) {
		player.inventory.setInventorySlotContents(i, itemStack);
	}

}
