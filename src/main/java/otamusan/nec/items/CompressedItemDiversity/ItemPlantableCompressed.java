package otamusan.nec.items.CompressedItemDiversity;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import otamusan.nec.blocks.CompressedBlockDiversity.BlockCompressed;
import otamusan.nec.common.CommonProxy;
import otamusan.nec.common.Lib;
import otamusan.nec.common.config.NECConfig;
import otamusan.nec.items.UpdateCompressed;
import otamusan.nec.tileentity.ITileCompressed;
import otamusan.nec.tileentity.TileCompressed;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ItemPlantableCompressed extends ItemCompressed implements IItemCompressed , IPlantable {
    @Override
    public boolean isAvailable(Item item) {
        return item instanceof IPlantable;
    }
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = player.getHeldItem(hand);
        IBlockState state = worldIn.getBlockState(pos);

        if (facing == EnumFacing.UP && player.canPlayerEdit(pos.offset(facing), facing, itemstack) && state.getBlock().canSustainPlant(state, worldIn, pos, EnumFacing.UP, this) && worldIn.isAirBlock(pos.up())) {
            worldIn.setBlockState(pos.up(), CommonProxy.BLOCKCROP.getDefaultState());
            TileCompressed tileCompressed = (TileCompressed) worldIn.getTileEntity(pos.up());
            if (player instanceof EntityPlayerMP) {
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos.up(), itemstack);
            }
            ItemStack stack = itemstack.copy();
            stack.setCount(1);
            tileCompressed.setItemCompressed(stack);
            ItemStack original = ItemCompressed.getOriginal(stack);
            IBlockState oriblock = ((IPlantable)original.getItem()).getPlant(worldIn,pos.up());
            tileCompressed.setBlockState(oriblock.getBlock().getDefaultState());

            itemstack.shrink(1);
            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.FAIL;
        }
    }
    private ArrayList<IItemCompressed> children = new ArrayList<>();
    private IItemCompressed parent;

    @Override
    public ArrayList<IItemCompressed> getChildren() {
        return children;
    }

    @Override
    public String getName() {
        return "itemplantable";
    }

    @Override
    public IItemCompressed getParent() {
        return parent;
    }

    @Override
    public void setParent(IItemCompressed iItemCompressed) {
        this.parent = iItemCompressed;
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess iBlockAccess, BlockPos blockPos) {
        return EnumPlantType.Crop;
    }

    @Override
    public IBlockState getPlant(IBlockAccess iBlockAccess, BlockPos blockPos) {
        return CommonProxy.BLOCKCROP.getDefaultState();
    }
}
