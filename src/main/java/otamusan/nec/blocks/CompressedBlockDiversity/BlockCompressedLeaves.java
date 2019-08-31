package otamusan.nec.blocks.CompressedBlockDiversity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import otamusan.nec.tileentity.TileCompressed;

public class BlockCompressedLeaves extends BlockCompressed implements IBlockCompressed, IShearable {
	public BlockCompressedLeaves() {
	}

	@Override
	public boolean isAvailable(Block item) {
		return item instanceof BlockLeaves;
	}

	@Override
	public String getName() {
		return "blockleaves";
	}

	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileCompressed();
	}

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public boolean isLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
		List<ItemStack> newlist = new ArrayList<ItemStack>();

		return newlist;
	}
}
