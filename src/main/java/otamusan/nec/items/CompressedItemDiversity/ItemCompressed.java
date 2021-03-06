package otamusan.nec.items.CompressedItemDiversity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import otamusan.nec.blocks.CompressedBlockDiversity.BlockCompressed;
import otamusan.nec.blocks.CompressedBlockDiversity.IBlockCompressed;
import otamusan.nec.common.CommonProxy;
import otamusan.nec.common.Lib;
import otamusan.nec.items.UpdateCompressed;
import otamusan.nec.items.UsingCompressed;

public class ItemCompressed extends Item implements IItemCompressed {

    public ItemCompressed() {
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

    public static boolean isSpecialized(ItemStack compressed){
        if(compressed.getItem() != CommonProxy.ITEMBASE && compressed.getItem() != CommonProxy.ITEMBLOCK)
            return true;
        if(!(ItemCompressed.getOriginal(compressed).getItem() instanceof ItemBlock))
            return false;
        Block block = BlockCompressed.getOriginalBlock(compressed);
        return BlockCompressed.getBlockCompressed(block) != CommonProxy.BLOCKBASE;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getItemStackDisplayName(ItemStack compressed) {
        ItemStack itemStack = getOriginal(compressed);
        if (itemStack.isEmpty())
            return I18n.format(Lib.MOD_ID + ".hasnotitem");

        int time = getTime(compressed);
        if(isSpecialized(compressed)) {
            return "§b§l" + I18n.format(Lib.MOD_ID + ".compressed", time, itemStack.getDisplayName());
        }
        return I18n.format(Lib.MOD_ID + ".compressed", time, itemStack.getDisplayName());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack compressed = player.getHeldItem(hand);
        if (!compressed.isEmpty()) {
            return UsingCompressed.onItemRightClick(world, player, hand);
        }
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, compressed);
    }

    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
                                      EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack compressed = player.getHeldItem(hand);
        if (!compressed.isEmpty()) {

            return UsingCompressed.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);

        }
        return EnumActionResult.FAIL;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        UpdateCompressed.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    public int getItemBurnTime(ItemStack itemStack) {
        ItemStack original = ItemCompressed.getOriginal(itemStack);
        int time = ItemCompressed.getTime(itemStack);
        int burntime = ForgeEventFactory.getItemBurnTime(original);
        if (burntime == -1) {
            if (TileEntityFurnace.getItemBurnTime(original) == -1) {
                return -1;
            } else {
                burntime = TileEntityFurnace.getItemBurnTime(original);
            }
        }
        return (int) Math.pow(burntime, time);
    }

    public int getItemStackLimit(ItemStack stack) {
        return ItemCompressed.getOriginal(stack).getItem().getItemStackLimit(stack);
    }

    public BlockPos getPlacedPos(World worldIn, BlockPos pos, EnumFacing facing) {
        if (!worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos)) {
            return pos.offset(facing);
        }
        return pos;
    }

    public static Item getCompressedItem(Item item){
        return CommonProxy.ITEMBASE.getItem(item);
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack compressed, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (!compressed.hasTagCompound())
            return;
        if(isSpecialized(compressed)){
            boolean isblock = ItemCompressed.getOriginal(compressed).getItem() instanceof ItemBlock;
            if(isblock){
                tooltip.add("§6"+I18n.format(Lib.MOD_ID + ".blockspecialized"));
                tooltip.add("§6"+I18n.format(Lib.MOD_ID + ".blockdescription."+BlockCompressed.getBlockCompressed(compressed).getRegistryName()));

            }else{
                tooltip.add("§6"+I18n.format(Lib.MOD_ID + ".itemspecialized"));
                tooltip.add("§6"+I18n.format(Lib.MOD_ID + ".itemdescription."+compressed.getItem().getRegistryName()));
            }
        }
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

        if (!isCompressedItem(uncompressed)) {
            compressed = new ItemStack(CommonProxy.ITEMBASE.getItem(uncompressed.getItem()));
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

    public static @Nonnull
    ItemStack getOriginal(final @Nonnull ItemStack item) {
        if (!isCompressedItem(item))
            return ItemStack.EMPTY;
        NBTTagCompound nbt = item.getTagCompound();
        if (nbt == null)
            return ItemStack.EMPTY;
        return new ItemStack(nbt.getCompoundTag(Lib.MOD_ID + "_itemstack"));
    }

    public static int getTime(final @Nonnull ItemStack item) {
        if (!isCompressedItem(item))
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

    @Override
    public boolean isAvailable(Item item) {
        return true;
    }

    private ArrayList<IItemCompressed> children = new ArrayList<>();
    private IItemCompressed parent;

    @Override
    public ArrayList<IItemCompressed> getChildren() {
        return children;
    }

    public static boolean isCompressedItem(ItemStack itemStack) {
        return itemStack.getItem() instanceof IItemCompressed;
    }

    public static boolean isCompressedItem(Item item) {
        return item instanceof IItemCompressed;
    }

    public static ArrayList<Item> getCompressedList() {
        return CommonProxy.ITEMBASE.getCompressedItems();
    }

    public static ArrayList<IItemCompressed> getCompressedItemList() {
        return CommonProxy.ITEMBASE.getCompressedItemCompressed();
    }

    @Override
    public String getName() {
        return "base";
    }

    @Override
    public IItemCompressed getParent() {
        return parent;
    }

    @Override
    public void setParent(IItemCompressed iItemCompressed) {
        parent = iItemCompressed;
    }

}
