package otamusan.nec.world;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import otamusan.nec.common.CommonProxy;
import otamusan.nec.common.config.NECConfig;
import otamusan.nec.items.CompressedItemDiversity.ItemCompressed;
import otamusan.nec.tileentity.ITileCompressed;

public class CompressedGenerator implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		int x = chunkX;
		int z = chunkZ;
		int sx = new ChunkPos(x, z).getXStart();
		int sz = new ChunkPos(x, z).getZStart();
		int ex = new ChunkPos(x, z).getXEnd();
		int ez = new ChunkPos(x, z).getZEnd();

		if (!NECConfig.CONFIG_TYPES.world.isReplaceChunks)
			return;

		if (random.nextDouble() > NECConfig.CONFIG_TYPES.world.rate)
			return;

		int time = NECConfig.CONFIG_TYPES.world.maxTimeReplaced - (int) Math.floor(Math.log(random.nextInt(
				(int) Math.pow(NECConfig.CONFIG_TYPES.world.deviationofTime,
						NECConfig.CONFIG_TYPES.world.maxTimeReplaced))
				+ 1)
				/ Math.log(NECConfig.CONFIG_TYPES.world.deviationofTime));
		for (int ix = sx; ix <= ex; ix++) {
			for (int iz = sz; iz <= ez; iz++) {
				for (int iy = 0; iy <= 256; iy++) {

					BlockPos pos = new BlockPos(ix, iy, iz);

					if (!isReplaceable(pos, world)) {
						continue;
					}

					replace(pos, world, time);
				}
			}
		}

	}

	public boolean replace(BlockPos pos, World world, int time) {

		IBlockState state = world.getBlockState(pos);

		ItemStack item = state.getBlock().getItem(world, pos, state);

		world.setBlockState(pos, CommonProxy.BLOCKBASE.getBlock(state.getBlock()).getDefaultState());

		ITileCompressed tileCompressed = (ITileCompressed) world.getTileEntity(pos);

		if (tileCompressed == null) {
			world.setBlockState(pos, state);
			return false;
		}

		tileCompressed.setBlockState(state);

		ItemStack compressed = ItemCompressed.createCompressedItem(item, time);

		tileCompressed.setItemCompressed(compressed);
		return true;
	}

	public boolean isReplaceable(BlockPos pos, World world) {

		IBlockState state = world.getBlockState(pos);

		ItemStack item = state.getBlock().getItem(world, pos, state);

		return !item.isEmpty() && !state.getBlock().isAir(state, world, pos)
				&& state.getBlock() != CommonProxy.BLOCKBASE.getBlock(state.getBlock())
				&& state.getBlock().getBlockHardness(state, world, pos) != -1
				&& NECConfig.isCompressible(item.getItem()) && NECConfig.isPlacable(state.getBlock());
	}
}