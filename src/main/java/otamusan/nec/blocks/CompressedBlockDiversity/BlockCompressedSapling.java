package otamusan.nec.blocks.CompressedBlockDiversity;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenBirchTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.BlockSnapshot;
import otamusan.nec.common.CommonProxy;
import otamusan.nec.common.config.NECConfig;
import otamusan.nec.items.CompressedItemDiversity.ItemCompressed;
import otamusan.nec.tileentity.ITileCompressed;
import otamusan.nec.tileentity.TileCompressed;

public class BlockCompressedSapling extends BlockCompressed implements IBlockCompressed, IPlantable, IGrowable {
    public BlockCompressedSapling() {
        super(Material.PLANTS);
        setTickRandomly(true);
    }

    @Override
    public boolean isAvailable(Block item) {
        return item == Blocks.SAPLING;
    }

    @Override
    public String getName() {
        return "blocksapling";
    }

    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileCompressed();
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        IBlockState soil = worldIn.getBlockState(pos.down());
        return super.canPlaceBlockAt(worldIn, pos)
                && soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
    }

    protected boolean canSustainBush(IBlockState state) {
        return state.getBlock() == Blocks.GRASS || state.getBlock() == Blocks.DIRT
                || state.getBlock() == Blocks.FARMLAND;
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
        if (state.getBlock() == this) {
            IBlockState soil = worldIn.getBlockState(pos.down());
            return soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
        }
        return this.canSustainBush(worldIn.getBlockState(pos.down()));
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Plains;
    }

    @Override
    public IBlockState getPlant(net.minecraft.world.IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() != this)
            return getDefaultState();
        return state;
    }

    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return true;
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return (double) worldIn.rand.nextFloat() < (0.45D / (Math.pow(8,
                ItemCompressed.getTime(((ITileCompressed) worldIn.getTileEntity(pos)).getItemCompressed()))));
    }

    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        this.grow(worldIn, pos, state, rand);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {

            int p = rand.nextInt(getTime(worldIn, pos));

            if (NECConfig.CONFIG_TYPES.using.isSaplingSlowDownGrowth && p != 0)
                return;

            this.checkAndDropBlock(worldIn, pos, state);

            if (!worldIn.isAreaLoaded(pos, 1))
                return;
            if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0) {
                this.grow(worldIn, pos, state, rand);
            }
        }
    }

    public void grow(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        int p = rand.nextInt(getTime(worldIn, pos));

        if (NECConfig.CONFIG_TYPES.using.isSaplingSlowDownGrowth && p != 0)
            return;
        IBlockState ori = getOriginalBlockState(worldIn, pos);
        int time = getTime(worldIn, pos);

        if (((Integer) ori.getValue(BlockSapling.STAGE)).intValue() == 0) {
            //worldIn.setBlockState(pos, ori.cycleProperty(BlockSapling.STAGE), 4);
            ((ITileCompressed) worldIn.getTileEntity(pos)).setBlockState(ori.cycleProperty(BlockSapling.STAGE));

        } else {
            worldIn.capturedBlockSnapshots.clear();
            worldIn.captureBlockSnapshots = true;

            //this.generateTree(worldIn, pos, ori, rand);
            ((BlockSapling) (ori.getBlock())).generateTree(worldIn, pos, ori, rand);
            worldIn.captureBlockSnapshots = false;

            ArrayList<BlockSnapshot> snapshots = (ArrayList<BlockSnapshot>) worldIn.capturedBlockSnapshots.clone();
            ArrayList<BlockSnapshot> newsnapshots = new ArrayList<>();
            for (BlockSnapshot snapshot : snapshots) {
                if (newsnapshots.stream().anyMatch(newsnapshot -> newsnapshot.getPos().equals(snapshot.getPos())))
                    continue;
                newsnapshots.add(snapshot);
            }

            for (BlockSnapshot block : newsnapshots) {

                //IBlockState oldstate = block.getCurrentBlock();
                IBlockState oldstate = worldIn.getBlockState(block.getPos());
                if (oldstate.getBlock() == Blocks.DIRT) continue;
                ItemStack item = oldstate.getBlock().getItem(worldIn, pos, oldstate);

                IBlockState newstate = BlockCompressed.getBlockCompressed(oldstate.getBlock()).getDefaultState();
                worldIn.setBlockState(block.getPos(), newstate, 11);
                ITileCompressed compressed = (ITileCompressed) worldIn.getTileEntity(block.getPos());
                compressed.setBlockState(oldstate);
                compressed.setNatural(true);
                compressed.setItemCompressed(ItemCompressed.createCompressedItem(item, time));
                ((TileEntity) compressed).markDirty();
            }

        }

    }

    public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(worldIn, rand, pos))
            return;
        int time = ItemCompressed.getTime(((ITileCompressed) worldIn.getTileEntity(pos)).getItemCompressed());

        WorldGenerator worldgenerator = (WorldGenerator) (rand.nextInt(10) == 0 ? new WorldGenBigTree(true)
                : new WorldGenTrees(true));
        int i = 0;
        int j = 0;
        boolean flag = false;

        switch ((BlockPlanks.EnumType) state.getValue(BlockSapling.TYPE)) {
            case SPRUCE:
                label68:

                for (i = 0; i >= -1; --i) {
                    for (j = 0; j >= -1; --j) {
                        if (this.isTwoByTwoOfType(worldIn, pos, i, j, BlockPlanks.EnumType.SPRUCE, time)) {
                            worldgenerator = new WorldGenMegaPineTree(true, rand.nextBoolean());
                            flag = true;
                            break label68;
                        }
                    }
                }

                if (!flag) {
                    i = 0;
                    j = 0;
                    worldgenerator = new WorldGenTaiga2(true);
                }

                break;
            case BIRCH:
                worldgenerator = new WorldGenBirchTree(true, false);
                break;
            case JUNGLE:
                IBlockState iblockstate = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT,
                        BlockPlanks.EnumType.JUNGLE);
                IBlockState iblockstate1 = Blocks.LEAVES.getDefaultState()
                        .withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE)
                        .withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
                label82:

                for (i = 0; i >= -1; --i) {
                    for (j = 0; j >= -1; --j) {
                        if (this.isTwoByTwoOfType(worldIn, pos, i, j, BlockPlanks.EnumType.JUNGLE, time)) {
                            worldgenerator = new WorldGenMegaJungle(true, 10, 20, iblockstate, iblockstate1);
                            flag = true;
                            break label82;
                        }
                    }
                }

                if (!flag) {
                    i = 0;
                    j = 0;
                    worldgenerator = new WorldGenTrees(true, 4 + rand.nextInt(7), iblockstate, iblockstate1, false);
                }

                break;
            case ACACIA:
                worldgenerator = new WorldGenSavannaTree(true);
                break;
            case DARK_OAK:
                label96:

                for (i = 0; i >= -1; --i) {
                    for (j = 0; j >= -1; --j) {
                        if (this.isTwoByTwoOfType(worldIn, pos, i, j, BlockPlanks.EnumType.DARK_OAK, time)) {
                            worldgenerator = new WorldGenCanopyTree(true);
                            flag = true;
                            break label96;
                        }
                    }
                }

                if (!flag) {
                    return;
                }

            case OAK:
        }

        IBlockState iblockstate2 = Blocks.AIR.getDefaultState();

        if (flag) {
            worldIn.setBlockState(pos.add(i, 0, j), iblockstate2, 4);
            worldIn.setBlockState(pos.add(i + 1, 0, j), iblockstate2, 4);
            worldIn.setBlockState(pos.add(i, 0, j + 1), iblockstate2, 4);
            worldIn.setBlockState(pos.add(i + 1, 0, j + 1), iblockstate2, 4);
        } else {

            worldIn.setBlockState(pos, iblockstate2, 4);
        }
        worldIn.capturedBlockSnapshots.clear();
        worldIn.captureBlockSnapshots = true;

        boolean result = worldgenerator.generate(worldIn, rand, pos.add(i, 0, j));
        replace(worldIn, time);

        if (!result) {

            if (flag) {

                //worldIn.setBlockState(pos.add(i, 0, j), CommonProxy.BLOCKSAPLING.getDefaultState(), 4);
                //worldIn.setBlockState(pos.add(i + 1, 0, j), CommonProxy.BLOCKSAPLING.getDefaultState(), 4);
                //worldIn.setBlockState(pos.add(i, 0, j + 1), CommonProxy.BLOCKSAPLING.getDefaultState(), 4);
                //worldIn.setBlockState(pos.add(i + 1, 0, j + 1), CommonProxy.BLOCKSAPLING.getDefaultState(), 4);
                ((ITileCompressed) worldIn.getTileEntity(pos.add(i, 0, j))).setBlockState(state);
                ((ITileCompressed) worldIn.getTileEntity(pos.add(i + 1, 0, j))).setBlockState(state);
                ((ITileCompressed) worldIn.getTileEntity(pos.add(i, 0, j + 1))).setBlockState(state);
                ((ITileCompressed) worldIn.getTileEntity(pos.add(i + 1, 0, j + 1))).setBlockState(state);

                ((ITileCompressed) worldIn.getTileEntity(pos.add(i, 0, j))).setItemCompressed(
                        ItemCompressed.createCompressedItem(state.getBlock().getItem(worldIn, pos, state), time));
                ((ITileCompressed) worldIn.getTileEntity(pos.add(i + 1, 0, j))).setItemCompressed(
                        ItemCompressed.createCompressedItem(state.getBlock().getItem(worldIn, pos, state), time));
                ((ITileCompressed) worldIn.getTileEntity(pos.add(i, 0, j + 1))).setItemCompressed(
                        ItemCompressed.createCompressedItem(state.getBlock().getItem(worldIn, pos, state), time));
                ((ITileCompressed) worldIn.getTileEntity(pos.add(i + 1, 0, j + 1))).setItemCompressed(
                        ItemCompressed.createCompressedItem(state.getBlock().getItem(worldIn, pos, state), time));

            } else {
                //worldIn.setBlockState(pos, state, 4);
                //worldIn.setBlockState(pos, CommonProxy.BLOCKSAPLING.getDefaultState(), 4);

                ((ITileCompressed) worldIn.getTileEntity(pos)).setBlockState(state);
                ((ITileCompressed) worldIn.getTileEntity(pos)).setItemCompressed(
                        ItemCompressed.createCompressedItem(state.getBlock().getItem(worldIn, pos, state), time));

            }

        }

    }

    private static void replace(World worldIn, int time) {

        ArrayList<BlockSnapshot> snapshots = (ArrayList<BlockSnapshot>) worldIn.capturedBlockSnapshots.clone();
        worldIn.captureBlockSnapshots = false;

        //if (worldIn.isRemote)
        //	return;

        ArrayList<BlockPos> poss = new ArrayList<>();

        lab:
        for (BlockSnapshot block : snapshots) {
            for (BlockPos blockPos : poss) {
                if (blockPos.equals(block.getPos())) {
                    continue lab;
                }
            }
            poss.add(block.getPos());

            IBlockState oldstate = worldIn.getBlockState(block.getPos());

            if (oldstate.getBlock() == Blocks.DIRT)
                return;

            ItemStack item = oldstate.getBlock().getItem(worldIn, block.getPos(), oldstate);
            //System.out.println(item);

            IBlockState newstate = CommonProxy.BLOCKBASE.getBlock(oldstate.getBlock()).getDefaultState();

            //if (newstate == oldstate)
            //	continue lab;
            //if (oldstate instanceof IBlockCompressed)
            //	continue lab;

            worldIn.setBlockState(block.getPos(), newstate, 11);
            //worldIn.setBlockState(block.getPos(), Blocks.CLAY.getDefaultState());

            ITileCompressed compressed = (ITileCompressed) worldIn.getTileEntity(block.getPos());
            compressed.setBlockState(oldstate);

            compressed.setItemCompressed(ItemCompressed.createCompressedItem(item, time));

            compressed.setNatural(true);

            //worldIn.markAndNotifyBlock(block.getPos(), worldIn.getChunkFromBlockCoords(block.getPos()), oldstate,
            //		newstate, 11);
            //Minecraft.getMinecraft().renderGlobal.markBlockRangeForRenderUpdate(time, time, time, time, time, time);
            ((TileEntity) compressed).markDirty();

			/*for (EntityPlayer player : worldIn.playerEntities) {
				if (player instanceof EntityPlayerMP) {
					EntityPlayerMP entityPlayer = (EntityPlayerMP) player;
					entityPlayer.connection.sendPacket(new SPacketBlockChange(worldIn, block.getPos()));
					entityPlayer.connection.sendPacket(((TileEntity) compressed).getUpdatePacket());
				}
			}*/

			/*try {
				Minecraft.getMinecraft().renderGlobal.markBlockRangeForRenderUpdate(block.getPos().getX() - 1,
						block.getPos().getY() - 5,
						block.getPos().getZ() - 5,
						block.getPos().getX() + 5, block.getPos().getY() + 5, block.getPos().getZ() + 5);
			} catch (Exception e) {
			
			}*/
        }
    }

    private boolean isTwoByTwoOfType(World worldIn, BlockPos pos, int p_181624_3_, int p_181624_4_,
                                     BlockPlanks.EnumType type, int time) {
        return this.isTypeAt(worldIn, pos.add(p_181624_3_, 0, p_181624_4_), type, time)
                && this.isTypeAt(worldIn, pos.add(p_181624_3_ + 1, 0, p_181624_4_), type, time)
                && this.isTypeAt(worldIn, pos.add(p_181624_3_, 0, p_181624_4_ + 1), type, time)
                && this.isTypeAt(worldIn, pos.add(p_181624_3_ + 1, 0, p_181624_4_ + 1), type, time);
    }

    /**
     * Check whether the given BlockPos has a Sapling of the given type
     */
    public boolean isTypeAt(World worldIn, BlockPos pos, BlockPlanks.EnumType type, int time) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        IBlockState orBlockState = getOriginalBlockState(worldIn, pos);

        return iblockstate.getBlock() == this && orBlockState.getValue(BlockSapling.TYPE) == type
                && ItemCompressed.getTime(((ITileCompressed) worldIn.getTileEntity(pos)).getItemCompressed()) == time;
    }
}
