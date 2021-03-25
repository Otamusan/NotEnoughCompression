package otamusan.nec.blocks.CompressedBlockDiversity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCarrot;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import otamusan.nec.common.CommonProxy;
import otamusan.nec.common.config.NECConfig;
import otamusan.nec.items.CompressedItemDiversity.ItemCompressed;
import otamusan.nec.tileentity.ITileCompressed;
import otamusan.nec.tileentity.TileCompressed;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;

public class BlockCompressedCrops extends BlockCompressed implements IPlantable, IGrowable {
    public BlockCompressedCrops(){
        super(Material.PLANTS);
        setTickRandomly(true);
    }
    private ArrayList<IBlockCompressed> children = new ArrayList<>();
    private IBlockCompressed parent;


    @Override
    public boolean isAvailable(Block item) {
        return item instanceof BlockCrops;
    }

    @Override
    public ArrayList<IBlockCompressed> getChildren() {
        return children;
    }

    @Override
    public String getName() {
        return "blockcrop";
    }

    @Override
    public IBlockCompressed getParent() {
        return parent;
    }

    @Override
    public void setParent(IBlockCompressed iBlockCompressed) {
        parent = iBlockCompressed;
    }

    @Override
    public boolean canGrow(World world, BlockPos blockPos, IBlockState iBlockState, boolean b) {
        if(!(getOriginalBlockState(world, blockPos).getBlock() instanceof BlockCrops)) return false;
        return ((BlockCrops)getOriginalBlockState(world, blockPos).getBlock()).canGrow(world, blockPos, getOriginalBlockState(world, blockPos),b);
    }

    @Override
    public boolean canUseBonemeal(World world, Random random, BlockPos blockPos, IBlockState iBlockState) {
        if(!(getOriginalBlockState(world, blockPos).getBlock() instanceof BlockCrops)) return false;

        return ((BlockCrops)getOriginalBlockState(world, blockPos).getBlock()).canUseBonemeal(world, random, blockPos, getOriginalBlockState(world, blockPos));
    }
    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        int time = getTime(worldIn,pos);
        if(NECConfig.CONFIG_TYPES.using.isCropsSlowDownGrowth && rand.nextInt((int) Math.pow(8, time))!=0)
            return;
        IBlockState original = getOriginalBlockState(worldIn,pos);
        worldIn.setBlockState(pos,original);

        ((BlockCrops)original.getBlock()).grow(worldIn, rand, pos, original);

        IBlockState grown = worldIn.getBlockState(pos);
        placeCompressedBlock(worldIn,pos,grown,ItemCompressed.createCompressedItem(grown.getBlock().getItem(worldIn,pos,grown),time),true);


    }


    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        this.checkAndDropBlock(worldIn, pos, state);
        if(!(getOriginalBlockState(worldIn, pos).getBlock() instanceof BlockCrops)) return;
        int time = getTime(worldIn,pos);

        if(NECConfig.CONFIG_TYPES.using.isCropsSlowDownGrowth && rand.nextInt((int) Math.pow(8, time))!=0)
            return;

        IBlockState original = getOriginalBlockState(worldIn,pos);
        worldIn.setBlockState(pos,original);

        original.getBlock().updateTick(worldIn,pos,original,rand);

        IBlockState grown = worldIn.getBlockState(pos);

        placeCompressedBlock(worldIn,pos,grown,ItemCompressed.createCompressedItem(grown.getBlock().getItem(worldIn,pos,grown),time),true);

    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess iBlockAccess, BlockPos blockPos) {
        return EnumPlantType.Crop;
    }

    /*@Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess worldIn, BlockPos pos, IBlockState state, int fortune) {
        NonNullList<ItemStack> d = NonNullList.create();
        getOriginalBlockState(worldIn,pos).getBlock().getDrops(d,worldIn,pos,getOriginalBlockState(worldIn,pos), fortune);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
    }*/

    @Override
    public IBlockState getPlant(IBlockAccess iBlockAccess, BlockPos blockPos) {
        return CommonProxy.BLOCKCROP.getDefaultState();
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        IBlockState soil = worldIn.getBlockState(pos.down());
        return super.canPlaceBlockAt(worldIn, pos) && soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), EnumFacing.UP, this);
    }
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        this.checkAndDropBlock(worldIn, pos, state);
    }

    protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.canBlockStay(worldIn, pos, state)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }

    }

    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
        if(!(getOriginalBlockState(worldIn, pos).getBlock() instanceof BlockCrops)) return true;

        return ((BlockCrops)getOriginalBlockState(worldIn,pos).getBlock()).canBlockStay(worldIn,pos,getOriginalBlockState(worldIn,pos));
    }

}
