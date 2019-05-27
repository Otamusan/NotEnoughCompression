package otamusan.nec.blocks.CompressedBlockDiversity;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCompressedFurnace extends BlockCompressed implements ITileEntityProvider {
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote)
			return true;

		worldIn.createExplosion(playerIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5, true);

		return true;
	}

	private ArrayList<IBlockCompressed> children = new ArrayList<>();
	private IBlockCompressed parent;

	@Override
	public boolean isAvailable(Block item) {
		return item == Blocks.FURNACE;
	}

	@Override
	public ArrayList<IBlockCompressed> getChildren() {
		return children;
	}

	@Override
	public String getName() {
		return "blockfurnace";
	}

	@Override
	public IBlockCompressed getParent() {
		return parent;
	}

	@Override
	public void setParent(IBlockCompressed iBlockCompressed) {
		parent = iBlockCompressed;
	}
}
