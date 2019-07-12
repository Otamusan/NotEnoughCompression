package otamusan.nec.blocks.CompressedBlockDiversity;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;
import otamusan.nec.tileentity.TileCompressedFurnace;

public class BlockCompressedFurnace extends BlockCompressed implements IBlockCompressed {
	public BlockCompressedFurnace() {
	}

	@Override
	public boolean isAvailable(Block item) {
		return item == Blocks.FURNACE || item == Blocks.LIT_FURNACE;
	}

	@Override
	public String getName() {
		return "blockfurnace";
	}

	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileCompressedFurnace();
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			return true;
		} else {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileCompressedFurnace) {
				playerIn.displayGUIChest((TileCompressedFurnace) tileentity);
				playerIn.addStat(StatList.FURNACE_INTERACTION);
			}

			return true;
		}
	}

	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		IBlockState original = getOriginalBlockState(worldIn, pos);
		original.getBlock().randomDisplayTick(original, worldIn, pos, rand);

	}

	public static void setState(boolean active, World worldIn, BlockPos pos) {
		IBlockState iblockstate = ((BlockCompressedFurnace) worldIn.getBlockState(pos).getBlock())
				.getOriginalBlockState(worldIn, pos);
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (active) {
			((TileCompressedFurnace) tileentity)
					.setBlockState(Blocks.LIT_FURNACE.getDefaultState().withProperty(BlockFurnace.FACING,
							iblockstate.getValue(BlockFurnace.FACING)));
		} else {
			((TileCompressedFurnace) tileentity)
					.setBlockState(Blocks.FURNACE.getDefaultState().withProperty(BlockFurnace.FACING,
							iblockstate.getValue(BlockFurnace.FACING)));
		}
		//worldIn.setTileEntity(pos, tileentity);

		//worldIn.setBlockState(pos, CommonProxy.BLOCKFURNECE.getDefaultState(), 8);
		//onPlaceItemIntoWorld(worldIn, pos);

		for (EntityPlayer player : worldIn.playerEntities) {
			if (player instanceof EntityPlayerMP) {
				EntityPlayerMP entityPlayer = (EntityPlayerMP) player;
				entityPlayer.connection.sendPacket(new SPacketBlockChange(worldIn, pos));
			}
		}
		//onPlaceItemIntoWorld(worldIn, pos);

		//worldIn.markBlockRangeForRenderUpdate(pos, pos);
		//worldIn.markAndNotifyBlock(pos, null, iblockstate, iblockstate, 0);
		try {
			Minecraft.getMinecraft().renderGlobal.markBlockRangeForRenderUpdate(pos.getX() - 1, pos.getY() - 1,
					pos.getZ() - 1,
					pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
		} catch (Exception e) {
		}

		//System.out.println(((ITileCompressed) worldIn.getTileEntity(pos)).getState());

	}

	public static void onPlaceItemIntoWorld(@Nonnull World world, @Nonnull BlockPos pos) {
		// handle all placement events here

		@SuppressWarnings("unchecked")
		List<BlockSnapshot> blockSnapshots = (List<BlockSnapshot>) world.capturedBlockSnapshots.clone();
		world.capturedBlockSnapshots.clear();

		for (BlockSnapshot blocksnapshot : Lists.reverse(blockSnapshots)) {
			world.restoringBlockSnapshots = true;
			blocksnapshot.restore(true, false);
			world.restoringBlockSnapshots = false;
		}

		for (BlockSnapshot snap : blockSnapshots) {
			int updateFlag = snap.getFlag();
			IBlockState oldBlock = snap.getReplacedBlock();
			IBlockState newBlock = world.getBlockState(snap.getPos());
			if (!newBlock.getBlock().hasTileEntity(newBlock)) // Containers get placed automatically
			{
				newBlock.getBlock().onBlockAdded(world, snap.getPos(), newBlock);
			}

			world.markAndNotifyBlock(snap.getPos(), null, oldBlock, newBlock, updateFlag);
		}
		world.capturedBlockSnapshots.clear();
	}
}
