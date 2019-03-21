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
import otamusan.nec.items.ItemCompressed;
import otamusan.nec.tileentity.TileCompressed;

public class CompressedGenerator implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		int x = chunkX;
		int z = chunkZ;
		int sx = new ChunkPos(x, z).getXStart();
		int sz = new ChunkPos(x, z).getZStart();

		for (int i = 0; i < NECConfig.CONFIG_TYPES.replacetime; i++) {

			int cx = (int) (16 * random.nextDouble()) + sx;
			int cz = (int) (16 * random.nextDouble()) + sz;
			int cy = (int) (255 * random.nextDouble());

			int time = 5 - (int) Math.floor(Math.log(random.nextInt(1024) + 1) / Math.log(4));

			BlockPos pos = new BlockPos(cx, cy, cz);

			IBlockState state = world.getBlockState(pos);

			ItemStack item = state.getBlock().getItem(world, pos, state);

			if (!isReplaceable(pos, world)) {
				i--;
				continue;
			}

			world.setBlockState(pos, CommonProxy.blockCompressed.getDefaultState());

			TileCompressed tileCompressed = (TileCompressed) world.getTileEntity(pos);

			if (tileCompressed == null) {
				world.setBlockState(pos, state);
				i--;
				continue;
			}

			tileCompressed.setBlockState(state);

			ItemStack compressed = ItemCompressed.createCompressedItem(item, time);

			tileCompressed.setItemCompressed(compressed);

		}

	}

	public boolean isReplaceable(BlockPos pos, World world) {

		IBlockState state = world.getBlockState(pos);

		ItemStack item = state.getBlock().getItem(world, pos, state);

		return !item.isEmpty() && !state.getBlock().isAir(state, world, pos)
				&& state.getBlock() != CommonProxy.blockCompressed
				&& state.getBlock().getBlockHardness(state, world, pos) != -1
				&& NECConfig.isCompressible(item.getItem()) && NECConfig.isPlacable(state.getBlock());
	}
}