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
	public ItemStack compressed;

	public ArrayList<CompressedItems> remains;

	public UsingCompressed(ItemStack compressed) {
		this.compressed = compressed;
		this.remains = new ArrayList<CompressedItems>();
	}

	public EnumActionResult useOnBlockStart(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		InventoryPlayer inv = player.inventory;
		EnumActionResult actionResult = useOnBlock(ItemCompressed.getTime(compressed), player, worldIn, pos, hand,
				facing, hitX, hitY, hitZ);
		compressed.shrink(1);
		inv.setInventorySlotContents(inv.currentItem, compressed);
		if (!worldIn.isRemote) {
			for (CompressedItems compressedItems : remains) {
				System.out.println(compressedItems.getCompressed());
				List<ItemStack> itemStacks = InventoryUtil.putStacksInSlots(inv, compressedItems.getCompressed());
				for (ItemStack itemStack : itemStacks) {
					Block.spawnAsEntity(worldIn, pos.add(0.5, 0.5, 0.5), itemStack);
				}
			}
		}

		return actionResult;
	}

	public EnumActionResult useOnBlock(int time, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (time != 0) {
			EnumActionResult actionResult = EnumActionResult.PASS;
			for (int i = 0; i < 8; i++) {
				EnumActionResult per = useOnBlock(time - 1, player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
				if (per == EnumActionResult.SUCCESS) {
					actionResult = EnumActionResult.SUCCESS;
				}
			}
			return actionResult;
		} else {
			InventoryPlayer inv = player.inventory;
			ItemStack ori = ItemCompressed.getOriginal(compressed).copy();
			ori.setCount(1);
			setItemToPlayerInv(player, ori, hand);
			EnumActionResult actionResult = ori.getItem().onItemUse(player, worldIn, pos, hand, facing, hitX, hitY,
					hitZ);
			addRemain(inv.getStackInSlot(inv.currentItem));
			setItemToPlayerInv(player, ItemStack.EMPTY, hand);
			return actionResult;
		}
	}

	public ActionResult<ItemStack> useStart(World world, EntityPlayer player, EnumHand hand) {
		InventoryPlayer inv = player.inventory;
		ActionResult<ItemStack> actionResult = use(ItemCompressed.getTime(compressed), world, player, hand);
		compressed.shrink(1);
		inv.setInventorySlotContents(inv.currentItem, compressed);
		if (!world.isRemote) {

			for (CompressedItems compressedItems : remains) {
				List<ItemStack> itemStacks = InventoryUtil.putStacksInSlots(inv, compressedItems.getCompressed());
				for (ItemStack itemStack : itemStacks) {
					Block.spawnAsEntity(world, player.getPosition().add(0.5, 0.5, 0.5), itemStack);
				}
			}
		}
		return actionResult;
	}

	public ActionResult<ItemStack> use(int time, World worldIn, EntityPlayer player, EnumHand hand) {
		if (time != 0) {
			ActionResult<ItemStack> actionResult = new ActionResult<ItemStack>(EnumActionResult.PASS,
					compressed.copy());
			for (int i = 0; i < 8; i++) {
				ActionResult<ItemStack> per = use(time - 1, worldIn, player, hand);
				if (per.getType() == EnumActionResult.SUCCESS) {
					actionResult = new ActionResult<ItemStack>(EnumActionResult.SUCCESS, compressed.copy());
				}
			}
			return actionResult;
		} else {
			InventoryPlayer inv = player.inventory;
			ItemStack ori = ItemCompressed.getOriginal(compressed).copy();
			ori.setCount(1);
			setItemToPlayerInv(player, ori, hand);
			ActionResult<ItemStack> actionResult = ori.getItem().onItemRightClick(worldIn, player, hand);
			addRemain(inv.getStackInSlot(inv.currentItem));
			setItemToPlayerInv(player, ItemStack.EMPTY, hand);
			return actionResult;
		}
	}

	public void setItemToPlayerInv(EntityPlayer player, ItemStack stack, EnumHand hand) {
		InventoryPlayer inv = player.inventory;
		if (hand == EnumHand.MAIN_HAND) {
			inv.setInventorySlotContents(inv.currentItem, stack);
		}
	}

	public void addRemain(ItemStack stack) {
		for (CompressedItems compressedItems : remains) {
			if (compressedItems.addCompressed(stack))
				return;
		}
		CompressedItems manager = new CompressedItems(stack.copy());
		manager.addCompressed(stack);
		remains.add(manager);
	}
}
