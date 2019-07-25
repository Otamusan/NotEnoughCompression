package otamusan.nec.blocks.CompressedBlockDiversity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import otamusan.nec.tileentity.TileCompressedHopper;

public class BlockCompressedHopper extends BlockCompressed implements IBlockCompressed {
	public BlockCompressedHopper() {
	}

	@Override
	public boolean isAvailable(Block item) {
		return item == Blocks.HOPPER;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) worldIn.getTileEntity(pos));

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public String getName() {
		return "blockhopper";
	}

	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileCompressedHopper();
	}

	public boolean isTopSolid(IBlockState state) {
		return true;
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			return true;
		} else {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileEntityHopper) {
				playerIn.displayGUIChest((TileEntityHopper) tileentity);
				playerIn.addStat(StatList.HOPPER_INSPECTED);
			}

			return true;
		}
	}
}
