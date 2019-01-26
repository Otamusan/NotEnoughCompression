package otamusan.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import otamusan.util.InventoryUtil;

public class UsingCompressed {

	public EnumActionResult useOnBlockStart(ItemStack compressed, EntityPlayer player, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ArrayList<CompressedItems> remains = new ArrayList<>();
		ArrayList<EnumActionResult> results = new ArrayList<>();
		useOnBlock(results, compressed, remains, ItemCompressed.getTime(compressed), player, worldIn, pos, hand, facing,
				hitX, hitY, hitZ);
		if (!worldIn.isRemote) {
			compressed.shrink(1);
			putRemains(player, remains);
		}

		for (EnumActionResult enumActionResult : results) {
			if (enumActionResult == EnumActionResult.SUCCESS)
				return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.FAIL;
	}

	public void useOnBlock(ArrayList<EnumActionResult> results, ItemStack compressed,
			ArrayList<CompressedItems> remains, int time, EntityPlayer player, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (time != 0) {
			for (int i = 0; i < 8; i++) {
				useOnBlock(results, compressed, remains, time - 1, player, worldIn, pos, hand, facing, hitX, hitY,
						hitZ);
			}
		} else {
			ItemStack ori = ItemCompressed.getOriginal(compressed).copy();
			ori.setCount(1);
			setItemToActive(player, ori, hand);
			EnumActionResult actionResult = ori.getItem().onItemUse(player, worldIn, pos, hand, facing, hitX, hitY,
					hitZ);
			// System.out.println(getItemToActive(player, hand));
			addRemain(getItemToActive(player, hand), remains);
			setItemToActive(player, ItemStack.EMPTY, hand);
			results.add(actionResult);
		}
	}

	public ActionResult<ItemStack> useStart(ItemStack compressed, World world, EntityPlayer player, EnumHand hand) {
		ArrayList<CompressedItems> remains = new ArrayList<>();
		ArrayList<ActionResult<ItemStack>> results = new ArrayList<>();
		use(results, compressed, remains, ItemCompressed.getTime(compressed), world, player, hand);

		if (!world.isRemote) {
			compressed.shrink(1);
			putRemains(player, remains);
		}

		for (ActionResult<ItemStack> actionResult : results) {
			if (actionResult.getType() == EnumActionResult.SUCCESS)
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, compressed);
		}

		return new ActionResult<ItemStack>(EnumActionResult.FAIL, compressed);
	}

	public void use(ArrayList<ActionResult<ItemStack>> results, ItemStack compressed,
			ArrayList<CompressedItems> remains, int time, World worldIn, EntityPlayer player, EnumHand hand) {
		if (time != 0) {
			for (int i = 0; i < 8; i++) {
				use(results, compressed, remains, time - 1, worldIn, player, hand);
			}
		} else {
			ItemStack ori = ItemCompressed.getOriginal(compressed).copy();
			ori.setCount(1);
			setItemToActive(player, ori, hand);
			ActionResult<ItemStack> actionResult = ori.getItem().onItemRightClick(worldIn, player, hand);
			addRemain(getItemToActive(player, hand), remains);
			// System.out.println(getItemToActive(player, hand));
			setItemToActive(player, ItemStack.EMPTY, hand);
			results.add(actionResult);
		}
	}

	public void putRemains(EntityPlayer player, ArrayList<CompressedItems> remains) {
		for (CompressedItems compressedItems : remains) {
			List<ItemStack> itemStacks = InventoryUtil.putStacksInSlots(player.inventory,
					compressedItems.getCompressed());
			for (ItemStack itemStack : itemStacks) {
				Block.spawnAsEntity(player.world, player.getPosition().add(0.5, 0.5, 0.5), itemStack);
			}
		}
	}

	public void setItemToActive(EntityPlayer player, ItemStack stack, EnumHand hand) {
		if (player.getEntityWorld().isRemote)
			return;
		InventoryPlayer inv = player.inventory;
		inv.setInventorySlotContents(inv.currentItem, stack);
	}

	public ItemStack getItemToActive(EntityPlayer player, EnumHand hand) {
		return player.getHeldItem(hand);
	}

	public void addRemain(ItemStack stack, ArrayList<CompressedItems> remains) {
		for (CompressedItems compressedItems : remains) {
			if (compressedItems.addCompressed(stack))
				return;
		}
		CompressedItems manager = new CompressedItems(stack.copy());
		manager.addCompressed(stack);
		remains.add(manager);
	}
}
