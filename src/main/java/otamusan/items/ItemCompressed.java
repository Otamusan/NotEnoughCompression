package otamusan.items;

import java.awt.Color;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import otamusan.NotEnoughCompression;
import otamusan.common.CommonProxy;
import otamusan.tileentity.TileCompressed;

public class ItemCompressed extends ItemBlock {

	public ItemCompressed(Block block) {
		super(block);
	}

	@Override
	public boolean isFull3D() {
		return true;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return getOriginal(stack).getItem().getDurabilityForDisplay(getOriginal(stack));
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return getOriginal(stack).getItem().showDurabilityBar(getOriginal(stack));
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return getOriginal(stack).getItem().getRGBDurabilityForDisplay(getOriginal(stack));
	}

	@Override
	public String getItemStackDisplayName(ItemStack compressed) {
		ItemStack itemStack = getOriginal(compressed);
		if (!itemStack.isEmpty()) {
			int time = getTime(compressed);
			return I18n.format(NotEnoughCompression.MOD_ID + ".compressed", time, itemStack.getDisplayName());
		}
		return I18n.format(NotEnoughCompression.MOD_ID + ".hasnotitem");
	}

	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack compressed = player.getHeldItem(hand);
		if (!compressed.isEmpty()) {
			ItemStack itemStack = ItemCompressed.getOriginal(compressed);
			if (itemStack.getItem() instanceof ItemBlock) {
				int meta = itemStack.getMetadata();
				IBlockState state = ((ItemBlock) itemStack.getItem()).getBlock().getStateForPlacement(worldIn, pos,
						facing, hitX, hitY, hitZ, meta, player, hand);
				super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
				BlockPos newpos = pos.offset(facing);
				TileCompressed tileCompressed = (TileCompressed) worldIn.getTileEntity(newpos);
				if (tileCompressed != null)
					tileCompressed.setBlockState(state);
				return EnumActionResult.SUCCESS;
			}
		}
		return EnumActionResult.FAIL;
	}

	@Override
	public void addInformation(ItemStack compressed, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (!compressed.hasTagCompound())
			return;
		ItemStack itemStack = getOriginal(compressed);
		if (!itemStack.isEmpty()) {
			int time = getTime(compressed);
			// int time = 2147483647;
			tooltip.add(I18n.format(NotEnoughCompression.MOD_ID + ".compresseditem", itemStack.getDisplayName()));
			tooltip.add(I18n.format(NotEnoughCompression.MOD_ID + ".total", getCompressedAmount(time)));
			ForgeEventFactory.onItemTooltip(itemStack, Minecraft.getMinecraft().player, tooltip, flagIn);
		}
	}

	private String getCompressedAmount(int time8) {
		double log10_8 = Math.log(8) / Math.log(10);
		double time10 = time8 * log10_8;
		int time10_int = (int) time10;

		int power_a;
		if (time10_int >= 6)
			power_a = time10_int % 3;
		else
			power_a = time10_int;

		double time10_fract = time10 - time10_int;
		double num_value = Math.pow(10, time10_fract);

		double pow_a = Math.pow(10, power_a);

		String str_value;
		if (time10_int >= 6)
			str_value = String.format("%.3f", num_value * pow_a);
		else
			str_value = String.format("%,.0f", num_value * pow_a);

		int pow_1000 = (time10_int - power_a) / 3;
		String str_1000;
		String key = NotEnoughCompression.MOD_ID + ".unit." + pow_1000;
		if (time10_int >= 6 && (!(I18n.hasKey(key)) || GuiScreen.isShiftKeyDown()))
			str_1000 = I18n.format(NotEnoughCompression.MOD_ID + ".unit.huge", pow_1000);
		else
			str_1000 = I18n.format(key, pow_1000);

		String str_num = I18n.format(NotEnoughCompression.MOD_ID + ".unit", str_value, str_1000);

		return str_num;
	}

	public static ItemStack createCompressedItem(ItemStack uncompressed) {
		return createCompressedItem(uncompressed, getTime(uncompressed) + 1);
	}

	public static ItemStack createCompressedItem(ItemStack uncompressed, int time) {
		ItemStack compressed;
		if (uncompressed.getItem() != CommonProxy.itemCompressed) {
			compressed = new ItemStack(CommonProxy.itemCompressed);
			setOriginal(compressed, uncompressed);
			setTime(compressed, time);
		} else {
			compressed = uncompressed.copy();
			setTime(compressed, time);
		}
		compressed.setCount(1);
		return compressed;
	}

	public static ItemStack createUncompressedItem(ItemStack compressed) {
		ItemStack uncompressed = null;
		int time = getTime(compressed);
		if (time >= 1) {
			if (time == 1) {
				ItemStack itemStack = getOriginal(compressed);
				uncompressed = itemStack.copy();
				uncompressed.setCount(8);
			} else {
				uncompressed = compressed.copy();
				uncompressed.setCount(8);
				setTime(uncompressed, time - 1);
			}
		}
		return uncompressed;
	}

	public static boolean isCompressedItemEqual(final @Nonnull ItemStack a, final @Nonnull ItemStack b) {
		if (!a.isItemEqual(b))
			return false;
		if (getTime(a) != getTime(b))
			return false;
		ItemStack item_a = getOriginal(a);
		ItemStack item_b = getOriginal(b);
		if (item_a.isEmpty() && item_b.isEmpty())
			return true;
		return item_a.isItemEqual(item_b);
	}

	public static @Nonnull ItemStack getOriginal(final @Nonnull ItemStack item) {
		if (item.getItem() != CommonProxy.itemCompressed)
			return ItemStack.EMPTY;
		NBTTagCompound nbt = item.getTagCompound();
		if (nbt == null)
			return ItemStack.EMPTY;
		return new ItemStack(nbt.getCompoundTag(NotEnoughCompression.MOD_ID + "_itemstack"));
	}

	public static int getTime(final @Nonnull ItemStack item) {
		if (item.getItem() != CommonProxy.itemCompressed)
			return 0;
		NBTTagCompound nbt = item.getTagCompound();
		if (nbt == null)
			return 0;
		return nbt.getInteger(NotEnoughCompression.MOD_ID + "_time");
	}

	private static void setOriginal(final @Nonnull ItemStack item, final @Nonnull ItemStack itemIn) {
		NBTTagCompound nbt = item.getTagCompound();
		if (nbt == null)
			nbt = new NBTTagCompound();

		ItemStack tmpItemIn = itemIn.copy();
		tmpItemIn.setCount(1);
		NBTTagCompound nbtItemIn = new NBTTagCompound();
		tmpItemIn.writeToNBT(nbtItemIn);

		nbt.setTag(NotEnoughCompression.MOD_ID + "_itemstack", nbtItemIn);
		item.setTagCompound(nbt);
	}

	private static void setTime(final @Nonnull ItemStack item, int time) {
		NBTTagCompound nbt = item.getTagCompound();
		if (nbt == null)
			nbt = new NBTTagCompound();
		nbt.setInteger(NotEnoughCompression.MOD_ID + "_time", time);
		item.setTagCompound(nbt);
	}

	public static Color getCompressedColor(Color source, int time) {
		float multi = 1.0f / (float) time;
		float r = ((float) source.getRed() / 255f) * (float) multi;
		float g = ((float) source.getGreen() / 255f) * (float) multi;
		float b = ((float) source.getBlue() / 255f) * (float) multi;
		return new Color(r, g, b);
	}

	public static Color getCompressedColor(int time) {
		return getCompressedColor(new Color(1f, 1f, 1f), time);
	}
}
