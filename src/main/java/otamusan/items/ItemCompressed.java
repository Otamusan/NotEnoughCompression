package otamusan.items;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import otamusan.NotEnoughCompression;
import otamusan.common.CommonProxy;
import otamusan.common.NECItems;
import otamusan.tileentity.TileCompressed;

public class ItemCompressed extends Item {

	@Override
	public boolean isFull3D() {
		return true;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		if (stack.getTagCompound() == null)
			return I18n.format(NotEnoughCompression.MOD_ID + ".hasnotitem");
		ItemStack itemStack = new ItemStack(
				stack.getTagCompound().getCompoundTag(NotEnoughCompression.MOD_ID + "_itemstack"));
		int time = stack.getTagCompound().getInteger(NotEnoughCompression.MOD_ID + "_time");
		String name = I18n.format(NotEnoughCompression.MOD_ID + ".compressed") + " " + itemStack.getDisplayName();
		return name;
	}

	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack compressed = player.getHeldItem(hand);
		ItemStack original = ItemCompressed.getOriginal(compressed);

		if (original.getItem() instanceof ItemBlock) {
			int meta = original.getMetadata();
			IBlockState state = ((ItemBlock) original.getItem()).getBlock().getStateForPlacement(worldIn, pos, facing,
					hitX, hitY, hitZ, meta, player, hand);
			CommonProxy.itemBlockCompressed.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
			TileCompressed tileCompressed = (TileCompressed) worldIn.getTileEntity(pos.offset(facing));
			if (tileCompressed == null)
				return EnumActionResult.SUCCESS;
			tileCompressed.setBlockState(state);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.FAIL;
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (!stack.hasTagCompound())
			return;
		ItemStack compressed = new ItemStack(
				(NBTTagCompound) stack.getTagCompound().getTag(NotEnoughCompression.MOD_ID + "_itemstack"));
		int time = stack.getTagCompound().getInteger(NotEnoughCompression.MOD_ID + "_time");

		tooltip.add(I18n.format(NotEnoughCompression.MOD_ID + ".compresseditem") + " : " + compressed.getDisplayName());
		tooltip.add(I18n.format(NotEnoughCompression.MOD_ID + ".total") + " : " + (int) Math.pow(8, time));
	}

	public static ItemStack createCompressedItem(ItemStack item) {
		ItemStack compressed = item.copy();
		compressed.setCount(1);
		NBTTagCompound nbt = new NBTTagCompound();
		ItemStack itemStack = new ItemStack(NECItems.itemcompressed);

		if (compressed.getItem() == NECItems.itemcompressed) {
			NBTTagCompound itemnbt = (NBTTagCompound) compressed.getTagCompound()
					.getTag(NotEnoughCompression.MOD_ID + "_itemstack");
			nbt.setTag(NotEnoughCompression.MOD_ID + "_itemstack", itemnbt);
			nbt.setInteger(NotEnoughCompression.MOD_ID + "_time",
					compressed.getTagCompound().getInteger(NotEnoughCompression.MOD_ID + "_time") + 1);
		} else {
			NBTTagCompound itemnbt = new NBTTagCompound();
			compressed.writeToNBT(itemnbt);
			nbt.setTag(NotEnoughCompression.MOD_ID + "_itemstack", itemnbt);
			nbt.setInteger(NotEnoughCompression.MOD_ID + "_time", 1);
		}
		itemStack.setTagCompound(nbt);
		return itemStack;
	}

	public static ItemStack createCompressedItem(ItemStack item, int time) {
		if (time <= 0) {
			ItemStack itemStack = item.copy();
			itemStack.setCount(0);
			return itemStack;
		}

		ItemStack compressed = item.copy();
		compressed.setCount(1);
		NBTTagCompound nbt = new NBTTagCompound();
		ItemStack itemStack = new ItemStack(NECItems.itemcompressed);

		NBTTagCompound itemnbt = new NBTTagCompound();
		compressed.writeToNBT(itemnbt);
		nbt.setTag(NotEnoughCompression.MOD_ID + "_itemstack", itemnbt);
		nbt.setInteger(NotEnoughCompression.MOD_ID + "_time", time);

		itemStack.setTagCompound(nbt);
		return itemStack;
	}

	public static ItemStack createUncompressedItem(ItemStack item) {
		ItemStack compressed = item.copy();
		NBTTagCompound nbt = compressed.getTagCompound();
		ItemStack uncompressed;
		if (nbt.getInteger(NotEnoughCompression.MOD_ID + "_time") == 1) {
			uncompressed = new ItemStack((NBTTagCompound) nbt.getTag(NotEnoughCompression.MOD_ID + "_itemstack"))
					.copy();
			uncompressed.setCount(8);
		} else {
			uncompressed = compressed.copy();
			uncompressed.setCount(8);
			uncompressed.getTagCompound().setInteger(NotEnoughCompression.MOD_ID + "_time",
					compressed.getTagCompound().getInteger(NotEnoughCompression.MOD_ID + "_time") - 1);
		}
		return uncompressed;
	}

	public static ItemStack getOriginal(ItemStack item) {
		NBTTagCompound nbt = item.getTagCompound();
		return new ItemStack((NBTTagCompound) nbt.getTag(NotEnoughCompression.MOD_ID + "_itemstack")).copy();
	}

	public static int getTime(ItemStack item) {
		NBTTagCompound nbt = item.getTagCompound();
		return nbt.getInteger(NotEnoughCompression.MOD_ID + "_time");
	}
}
