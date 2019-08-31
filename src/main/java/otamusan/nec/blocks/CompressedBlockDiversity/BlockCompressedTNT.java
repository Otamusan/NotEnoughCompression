package otamusan.nec.blocks.CompressedBlockDiversity;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import otamusan.nec.items.CompressedItemDiversity.ItemCompressed;
import otamusan.nec.tileentity.ITileCompressed;
import otamusan.nec.tileentity.TileCompressed;

public class BlockCompressedTNT extends BlockCompressed implements IBlockCompressed {
	public BlockCompressedTNT() {
	}

	@Override
	public boolean isAvailable(Block item) {
		return item == Blocks.TNT;
	}

	@Override
	public String getName() {
		return "blocktnt";
	}

	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileCompressed();
	}

	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);

		if (worldIn.isBlockPowered(pos)) {
			this.onBlockDestroyedByPlayer(worldIn, pos, state);
			worldIn.setBlockToAir(pos);
		}
	}

	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (worldIn.isBlockPowered(pos)) {
			this.onBlockDestroyedByPlayer(worldIn, pos, state);
			worldIn.setBlockToAir(pos);
		}
	}

	public void explode(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase igniter) {
		if (!worldIn.isRemote) {
			spawnTNTs(worldIn, pos, state, igniter,
					ItemCompressed.getTime(((ITileCompressed) worldIn.getTileEntity(pos)).getItemCompressed()));
			worldIn.playSound((EntityPlayer) null, pos.getX(), pos.getY(), pos.getZ(),
					SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);

		}
	}

	public static void spawnTNTs(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase igniter, int time) {

		if (time != 0) {

			for (int i = 0; i < 8; i++) {
				spawnTNTs(worldIn, pos, state, igniter, time - 1);
			}
		} else {
			EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(worldIn, (double) ((float) pos.getX() + 0.5F),
					(double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter);
			entitytntprimed.setFuse(entitytntprimed.getFuse() + new Random().nextInt(entitytntprimed.getFuse() / 4));
			worldIn.spawnEntity(entitytntprimed);
		}
	}

	public static void spawnChainTNTs(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase igniter,
			int time) {

		if (time != 0) {

			for (int i = 0; i < 8; i++) {
				spawnTNTs(worldIn, pos, state, igniter, time - 1);
			}
		} else {
			EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(worldIn, (double) ((float) pos.getX() + 0.5F),
					(double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter);
			entitytntprimed.setFuse(
					(short) (worldIn.rand.nextInt(entitytntprimed.getFuse() / 4) + entitytntprimed.getFuse() / 8));
			worldIn.spawnEntity(entitytntprimed);
		}
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack itemstack = playerIn.getHeldItem(hand);

		if (!itemstack.isEmpty()
				&& (itemstack.getItem() == Items.FLINT_AND_STEEL || itemstack.getItem() == Items.FIRE_CHARGE)) {
			this.explode(worldIn, pos, state, playerIn);
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);

			if (itemstack.getItem() == Items.FLINT_AND_STEEL) {
				itemstack.damageItem(1, playerIn);
			} else if (!playerIn.capabilities.isCreativeMode) {
				itemstack.shrink(1);
			}

			return true;
		} else {
			return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
		}
	}

	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (!worldIn.isRemote && entityIn instanceof EntityArrow) {
			EntityArrow entityarrow = (EntityArrow) entityIn;

			if (entityarrow.isBurning()) {
				this.explode(worldIn, pos, worldIn.getBlockState(pos),
						entityarrow.shootingEntity instanceof EntityLivingBase
								? (EntityLivingBase) entityarrow.shootingEntity
								: null);
				worldIn.setBlockToAir(pos);
			}
		}
	}

	public boolean canDropFromExplosion(Explosion explosionIn) {
		return false;
	}
}
