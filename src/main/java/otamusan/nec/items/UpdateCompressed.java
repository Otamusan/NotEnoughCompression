package otamusan.nec.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import otamusan.nec.common.config.NECConfig;
import otamusan.nec.items.UsingCompressed.Remains;

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

		long n = (long) Math.pow(8, ItemCompressed.getTime(compressed)) * compressed.getCount();

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

		if (remains.getCompresseds().size() == 1
				&& ItemStack.areItemStacksEqual(compressed, remains.getCompresseds().get(0))) {
			setItem(player, itemSlot, compressed);
		} else {
			UsingCompressed.putRemain(player, remains.getCompresseds());
		}
	}

	public static ItemStack getItem(EntityPlayer player, int i) {
		return player.inventory.getStackInSlot(i);
	}

	public static void setItem(EntityPlayer player, int i, ItemStack itemStack) {
		player.inventory.setInventorySlotContents(i, itemStack);
	}

}
