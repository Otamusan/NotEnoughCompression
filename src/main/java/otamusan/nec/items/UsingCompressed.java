package otamusan.nec.items;

import java.util.ArrayList;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import otamusan.nec.common.config.NECConfig;
import otamusan.nec.items.CompressedItemDiversity.ItemCompressed;

public class UsingCompressed {
	public static ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack compressed = player.getHeldItem(hand);
		ItemStack original = ItemCompressed.getOriginal(compressed);

		if (ItemCompressed.getTime(compressed) >= NECConfig.CONFIG_TYPES.UsingCompressionTime)
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, compressed);

		if (!NECConfig.isUsable(original.getItem()))
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, compressed);

		long n = (long) Math.pow(8, ItemCompressed.getTime(compressed));

		player.setHeldItem(hand, ItemStack.EMPTY);

		ArrayList<ActionResult<ItemStack>> results = new ArrayList<>();

		Remains remains = new Remains();

		for (int i = 0; i < n; i++) {
			ItemStack use = original.copy();
			use.setCount(1);
			setHeldItem(player, hand, use);
			results.add(use.useItemRightClick(world, player, hand));

			ItemStack remain = player.getHeldItem(hand);
			if (!remain.isEmpty()) {
				remains.addItem(remain.copy());
			}
			setHeldItem(player, hand, ItemStack.EMPTY);
		}

		if (remains.getCompresseds().size() == 1
				&& ItemStack.areItemStacksEqual(compressed, remains.getCompresseds().get(0))) {
			setHeldItem(player, hand, compressed);
		} else {
			ItemStack held = compressed.copy();
			held.shrink(1);
			setHeldItem(player, hand, held);
			putRemain(player, remains.getCompresseds());
		}

		for (ActionResult<ItemStack> actionResult : results) {
			if (actionResult.getType() == EnumActionResult.SUCCESS)
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, compressed);
		}

		return new ActionResult<ItemStack>(EnumActionResult.FAIL, compressed);
	}

	public static EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack compressed = player.getHeldItem(hand);
		ItemStack original = ItemCompressed.getOriginal(compressed);

		if (ItemCompressed.getTime(compressed) >= NECConfig.CONFIG_TYPES.UsingCompressionTime)
			return EnumActionResult.FAIL;

		if (!NECConfig.isUsable(original.getItem()))
			return EnumActionResult.FAIL;

		long n = (long) Math.pow(8, ItemCompressed.getTime(compressed));

		player.setHeldItem(hand, ItemStack.EMPTY);

		ArrayList<EnumActionResult> actionResults = new ArrayList<>();
		// CompressedItems remains = new CompressedItems(original.copy());
		Remains remains = new Remains();

		for (int i = 0; i < n; i++) {
			ItemStack use = original.copy();
			use.setCount(1);
			setHeldItem(player, hand, use);
			actionResults.add(use.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ));

			ItemStack remain = player.getHeldItem(hand);
			if (!remain.isEmpty()) {
				remains.addItem(remain.copy());
			}
			setHeldItem(player, hand, ItemStack.EMPTY);
		}

		if (remains.getCompresseds().size() == 1
				&& ItemStack.areItemStacksEqual(compressed, remains.getCompresseds().get(0))) {
			setHeldItem(player, hand, compressed);
		} else {
			compressed.shrink(1);
			setHeldItem(player, hand, compressed);
			putRemain(player, remains.getCompresseds());
		}

		System.out.println(player.getHeldItem(hand));

		for (EnumActionResult enumActionResult : actionResults) {
			if (enumActionResult == EnumActionResult.SUCCESS)
				return EnumActionResult.SUCCESS;
		}

		return EnumActionResult.PASS;
	}

	public static class Remains {
		ArrayList<CompressedItems> compressedItemss;

		public Remains() {
			compressedItemss = new ArrayList<>();
		}

		public void addItem(ItemStack stack) {
			for (CompressedItems compressedItems : compressedItemss) {
				if (compressedItems.isContainable(stack)) {
					compressedItems.addCompressed(stack.copy());
					return;
				}
			}

			CompressedItems newCI = new CompressedItems(stack.copy());
			newCI.addCompressed(stack.copy());
			compressedItemss.add(newCI);
		}

		public ArrayList<ItemStack> getCompresseds() {
			ArrayList<ItemStack> list = new ArrayList<>();

			for (CompressedItems compressedItems : compressedItemss) {
				list.addAll(compressedItems.getCompressed());
			}

			return list;
		}
	}

	public static void setItemStackToSlot(EntityPlayer player, EntityEquipmentSlot slotIn, ItemStack stack) {
		if (slotIn == EntityEquipmentSlot.MAINHAND) {
			player.inventory.mainInventory.set(player.inventory.currentItem, stack);
		} else if (slotIn == EntityEquipmentSlot.OFFHAND) {
			player.inventory.offHandInventory.set(0, stack);
		} else if (slotIn.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
			player.inventory.armorInventory.set(slotIn.getIndex(), stack);
		}
	}

	public static void setHeldItem(EntityPlayer player, EnumHand hand, ItemStack stack) {
		if (hand == EnumHand.MAIN_HAND) {
			setItemStackToSlot(player, EntityEquipmentSlot.MAINHAND, stack);
		} else {
			if (hand != EnumHand.OFF_HAND) {
				throw new IllegalArgumentException("Invalid hand " + hand);
			}

			setItemStackToSlot(player, EntityEquipmentSlot.OFFHAND, stack);
		}
	}

	public static void putRemain(EntityPlayer player, ArrayList<ItemStack> list) {
		for (ItemStack itemStack : list) {
			if (!player.inventory.addItemStackToInventory(itemStack.copy())) {
				if (!player.world.isRemote) {
					EntityItem item = new EntityItem(player.world, player.posX, player.posY, player.posZ, itemStack);
					player.world.spawnEntity(item);
				}
			}
		}
	}

}
