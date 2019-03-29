package otamusan.nec.items;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import otamusan.nec.common.CommonProxy;
import otamusan.nec.common.Lib;
import otamusan.nec.common.config.NECConfig;
import otamusan.nec.tileentity.TileCompressed;

public class ItemCompressed extends ItemBlock {

	public ItemCompressed(Block block) {
		super(block);
		setHasSubtypes(true);
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

	@SideOnly(Side.CLIENT)
	@Override
	public String getItemStackDisplayName(ItemStack compressed) {
		ItemStack itemStack = getOriginal(compressed);
		if (!itemStack.isEmpty()) {
			int time = getTime(compressed);
			return I18n.format(Lib.MOD_ID + ".compressed", time, itemStack.getDisplayName());
		}
		return I18n.format(Lib.MOD_ID + ".hasnotitem");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack compressed = player.getHeldItem(hand);
		if (!compressed.isEmpty() && !(ItemCompressed.getOriginal(compressed).getItem() instanceof ItemBlock)) {
			UsingCompressed.onItemRightClick(world, player, hand);
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, compressed);
	}

	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack compressed = player.getHeldItem(hand);
		if (!compressed.isEmpty()) {
			ItemStack itemStack = ItemCompressed.getOriginal(compressed);
			if (itemStack.getItem() instanceof ItemBlock && NECConfig.isPlacable(itemStack.getItem())) {
				int meta = itemStack.getMetadata();
				BlockPos newpos = getPlacedPos(worldIn, pos, facing);

				IBlockState state = ((ItemBlock) itemStack.getItem()).getBlock().getStateForPlacement(worldIn, newpos,
						facing, hitX, hitY, hitZ, meta, player, hand);
				super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);

				TileCompressed tileCompressed = (TileCompressed) worldIn.getTileEntity(newpos);
				if (tileCompressed != null)
					tileCompressed.setBlockState(state);
				return EnumActionResult.SUCCESS;
			} else {
				return UsingCompressed.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
			}
		}
		return EnumActionResult.FAIL;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		UpdateCompressed.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	public BlockPos getPlacedPos(World worldIn, BlockPos pos, EnumFacing facing) {
		if (!worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos)) {
			return pos.offset(facing);
		}
		return pos;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack compressed, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (!compressed.hasTagCompound())
			return;
		ItemStack itemStack = getOriginal(compressed);
		if (!itemStack.isEmpty()) {
			int time = getTime(compressed);
			// int time = 2147483647;
			tooltip.add(I18n.format(Lib.MOD_ID + ".compresseditem", itemStack.getDisplayName()));
			tooltip.add(I18n.format(Lib.MOD_ID + ".total", getCompressedAmount(time)));
			ForgeEventFactory.onItemTooltip(itemStack, Minecraft.getMinecraft().player, tooltip, flagIn);
		}

		List<String> oriInfo = itemStack.getTooltip(Minecraft.getMinecraft().player, flagIn);

		if (oriInfo.size() == 1)
			return;

		tooltip.add("");
		tooltip.add("=================");
		for (int i = 1; i < oriInfo.size(); i++) {
			if (i == 1 && oriInfo.get(i) == "")
				continue;
			tooltip.add("|| " + oriInfo.get(i));
		}
		tooltip.add("=================");

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
		String key = Lib.MOD_ID + ".unit." + pow_1000;
		if (time10_int >= 6 && (!(I18n.hasKey(key)) || GuiScreen.isShiftKeyDown()))
			str_1000 = I18n.format(Lib.MOD_ID + ".unit.huge", pow_1000);
		else
			str_1000 = I18n.format(key, pow_1000);

		String str_num = I18n.format(Lib.MOD_ID + ".unit", str_value, str_1000);

		return str_num;
	}

	public static ItemStack createCompressedItem(ItemStack uncompressed) {
		return createCompressedItem(uncompressed, getTime(uncompressed) + 1);
	}

	public static ItemStack createCompressedItem(ItemStack uncompressed, int time) {
		ItemStack compressed;
		if (time <= 0)
			return uncompressed.copy();

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
		return new ItemStack(nbt.getCompoundTag(Lib.MOD_ID + "_itemstack"));
	}

	public static int getTime(final @Nonnull ItemStack item) {
		if (item.getItem() != CommonProxy.itemCompressed)
			return 0;
		NBTTagCompound nbt = item.getTagCompound();
		if (nbt == null)
			return 0;
		return nbt.getInteger(Lib.MOD_ID + "_time");
	}

	private static void setOriginal(final @Nonnull ItemStack item, final @Nonnull ItemStack itemIn) {
		NBTTagCompound nbt = item.getTagCompound();
		if (nbt == null)
			nbt = new NBTTagCompound();

		ItemStack tmpItemIn = itemIn.copy();
		tmpItemIn.setCount(1);
		NBTTagCompound nbtItemIn = new NBTTagCompound();
		tmpItemIn.writeToNBT(nbtItemIn);

		nbt.setTag(Lib.MOD_ID + "_itemstack", nbtItemIn);
		item.setTagCompound(nbt);
	}

	private static void setTime(final @Nonnull ItemStack item, int time) {
		NBTTagCompound nbt = item.getTagCompound();
		if (nbt == null)
			nbt = new NBTTagCompound();
		nbt.setInteger(Lib.MOD_ID + "_time", time);
		item.setTagCompound(nbt);
	}

}
