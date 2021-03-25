package otamusan.nec.world;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import otamusan.nec.blocks.CompressedBlockDiversity.BlockCompressed;
import otamusan.nec.common.CommonProxy;
import otamusan.nec.common.config.NECConfig;
import otamusan.nec.items.CompressedItemDiversity.ItemCompressed;
import otamusan.nec.tileentity.ITileCompressed;

public class CompressedGenerator implements IWorldGenerator {
    public static final int maxsize = 7;

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
                         IChunkProvider chunkProvider) {
        int x = chunkX;
        int z = chunkZ;
        int sx = new ChunkPos(x, z).getXStart();
        int sz = new ChunkPos(x, z).getZStart();

        if (!NECConfig.CONFIG_TYPES.world.isReplaceChunks)
            return;

        if (random.nextDouble() > NECConfig.CONFIG_TYPES.world.rate)
            return;

        int time = NECConfig.CONFIG_TYPES.world.maxTimeReplaced - (int) Math.floor(Math.log(random.nextInt(
                (int) Math.pow(NECConfig.CONFIG_TYPES.world.deviationofTime,
                        NECConfig.CONFIG_TYPES.world.maxTimeReplaced))
                + 1)
                / Math.log(NECConfig.CONFIG_TYPES.world.deviationofTime));

		/*for (int ix = sx; ix <= ex; ix++) {
			for (int iz = sz; iz <= ez; iz++) {
				for (int iy = 0; iy <= 256; iy++) {

					BlockPos pos = new BlockPos(ix, iy, iz);

					if (isReplaceable(pos, world)) {
						replace(pos, world, time);
					}

				}
			}
		}*/

        int cx = x + random.nextInt(16) + sx;
        int cz = z + random.nextInt(16) + sz;
        int cy = random.nextInt(256);
        BlockPos cpos = new BlockPos(cx, cy, cz);
        int r = Math.round(maxsize / 2 + (maxsize / 2 * random.nextFloat()));
        for (int ix = cx - r; ix < cx + r + 1; ix++) {
            for (int iz = cz - r; iz < cz + r + 1; iz++) {
                for (int iy = cy - r; iy < cy + r + 1; iy++) {
                    BlockPos pos2 = new BlockPos(ix, iy, iz);

                    if (cpos.getDistance(ix, iy, iz) >= r) continue;
                    if (isReplaceable(pos2, world)) {
                        replace(pos2, world, time);

                    }
                }
            }
        }

    }

    public boolean replace(BlockPos pos, World world, int time) {

        IBlockState state = world.getBlockState(pos);

        ItemStack item = state.getBlock().getItem(world, pos, state);

        world.setBlockState(pos, BlockCompressed.getBlockCompressed(state.getBlock()).getDefaultState());

        ITileCompressed tileCompressed = (ITileCompressed) world.getTileEntity(pos);

        if (tileCompressed == null) {
            world.setBlockState(pos, state);
            return false;
        }

        tileCompressed.setBlockState(state);

        ItemStack compressed = ItemCompressed.createCompressedItem(item, time);

        tileCompressed.setItemCompressed(compressed);
        tileCompressed.setNatural(true);
        return true;
    }

    public boolean isReplaceable(BlockPos pos, World world) {

        IBlockState state = world.getBlockState(pos);

        ItemStack item = state.getBlock().getItem(world, pos, state);

        return (!item.isEmpty()

                && state.getBlock() != BlockCompressed.getBlockCompressed(state.getBlock())
                && state.getBlock().getBlockHardness(state, world, pos) != -1
                && NECConfig.isCompressible(item.getItem()) && NECConfig.isPlacable(state.getBlock()));
    }
}