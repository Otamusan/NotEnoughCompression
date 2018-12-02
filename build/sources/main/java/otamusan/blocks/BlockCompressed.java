package otamusan.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import otamusan.client.blockcompressed.UnlistedPropertyCompressedBlockNBT;
import otamusan.client.blockcompressed.UnlistedPropertyCompressedBlockState;
import otamusan.items.ItemCompressed;
import otamusan.tileentity.TileCompressed;

public class BlockCompressed extends Block implements ITileEntityProvider {

	public static final UnlistedPropertyCompressedBlockNBT COMPRESSEDBLOCK_NBT = new UnlistedPropertyCompressedBlockNBT();
	public static final UnlistedPropertyCompressedBlockState COMPRESSEDBLOCK_STATE = new UnlistedPropertyCompressedBlockState();

	public BlockCompressed() {
		super(Material.CIRCUITS);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		hasTileEntity = true;
	}

	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
		return 0.5f;
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state,
			int fortune) {
	}

	public IBlockState getOriginalBlockState(IBlockState state) {
		if (state instanceof IExtendedBlockState) {
			IExtendedBlockState estate = (IExtendedBlockState) state;

			if (estate.getValue(COMPRESSEDBLOCK_STATE) == null) {
				return Blocks.STONE.getDefaultState();
			}
			return estate.getValue(COMPRESSEDBLOCK_STATE);
		}
		return Blocks.STONE.getDefaultState();
	}

	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public float getEnchantPowerBonus(World world, BlockPos pos) {
		return super.getEnchantPowerBonus(world, pos);
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {

		ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
		int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, heldItem);
		int silktouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, heldItem);
		TileCompressed tileCompressed = (TileCompressed) worldIn.getTileEntity(pos);
		ItemStack itemCompressed = tileCompressed.compressedblock;
		IBlockState iBlockState = tileCompressed.getState();
		if (iBlockState == null)
			return;

		if (iBlockState.getBlock().canSilkHarvest(worldIn, pos, iBlockState, player) && silktouch == 0) {

			int time = ItemCompressed.getTime(tileCompressed.getItemCompressed());

			NonNullList<ItemStack> itemlist = NonNullList.create();

			iBlockState.getBlock().getDrops(itemlist, worldIn, pos, iBlockState, fortune);

			for (ItemStack itemStack : itemlist) {
				ItemStack compressed = ItemCompressed.createCompressedItem(itemStack, time);
				spawnAsEntity(worldIn, pos, compressed);
			}

		} else {
			itemCompressed.setCount(1);
			spawnAsEntity(worldIn, pos, itemCompressed.copy());
		}
	}

	@Override
	public boolean isOpaqueCube(IBlockState iBlockState) {
		return false;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {

		TileCompressed tile = (TileCompressed) worldIn.getTileEntity(pos);
		tile.setItemCompressed(stack);

	}

	@Override
	protected BlockStateContainer createBlockState() {
		IProperty[] listedProperties = new IProperty[0];
		IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[] { COMPRESSEDBLOCK_NBT, COMPRESSEDBLOCK_STATE };
		return new ExtendedBlockState(this, listedProperties, unlistedProperties);
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		if (state instanceof IExtendedBlockState) {
			IExtendedBlockState retval = (IExtendedBlockState) state;
			TileEntity tileEntity = world.getTileEntity(pos);
			TileCompressed tileCompressed = (TileCompressed) tileEntity;

			ItemStack compressed = tileCompressed.getItemCompressed();
			if (compressed != null) {
				NBTTagCompound itemNBT = new NBTTagCompound();
				compressed.writeToNBT(itemNBT);
				retval = retval.withProperty(COMPRESSEDBLOCK_NBT, itemNBT);
			}

			IBlockState blockState = tileCompressed.getState();
			if (state != null) {
				retval = retval.withProperty(COMPRESSEDBLOCK_STATE, blockState);
			}
			return retval;
		}
		return state;
	}

	@Override
	public boolean isFullCube(IBlockState iBlockState) {
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileCompressed();
	}

}
