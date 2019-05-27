package otamusan.nec.blocks.CompressedBlockDiversity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCompressedFurnace extends BlockCompressed {
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote)
			return true;

		worldIn.createExplosion(playerIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5, true);

		return true;
	}

	@Override
	public boolean isAvailable(Block item) {
		return item == Blocks.FURNACE;
	}

	@Override
	public String getName() {
		return "blockfurnace";
	}

}
