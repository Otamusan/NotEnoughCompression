package otamusan.nec.blocks.CompressedBlockDiversity;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import otamusan.nec.client.blockcompressed.UnlistedPropertyCompressedBlockNBT;
import otamusan.nec.client.blockcompressed.UnlistedPropertyCompressedBlockState;
import otamusan.nec.items.CompressedItemDiversity.ItemCompressed;
import otamusan.nec.tileentity.ITileCompressed;
import otamusan.nec.tileentity.TileCompressed;

public class BlockCompressed extends Block implements ITileEntityProvider, IBlockCompressed {

	public static final UnlistedPropertyCompressedBlockNBT COMPRESSEDBLOCK_NBT = new UnlistedPropertyCompressedBlockNBT();
	public static final UnlistedPropertyCompressedBlockState COMPRESSEDBLOCK_STATE = new UnlistedPropertyCompressedBlockState();

	public BlockCompressed() {
		super(Material.BARRIER);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		hasTileEntity = true;
	}

	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos) {
		if (((ITileCompressed) (worldIn.getTileEntity(pos))).isNatural()) {
			return (float) (blockStrength(state, player, worldIn, pos) / Math.pow(8, getTime(worldIn, pos)));
		}
		return blockStrength(state, player, worldIn, pos);
	}

	public float blockStrength(@Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull World world,
			@Nonnull BlockPos pos) {
		IBlockState original = getOriginalBlockState(world, pos);

		float hardness = original.getBlockHardness(world, pos);
		if (hardness < 0.0F) {
			return 0.0F;
		}

		if (!canHarvestBlock(original.getBlock(), player, world, pos)) {
			return player.getDigSpeed(original, pos) / hardness / 100F;
		} else {
			return player.getDigSpeed(original, pos) / hardness / 30F;
		}
	}

	public boolean canHarvestBlock(@Nonnull Block block, @Nonnull EntityPlayer player,
			@Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
		IBlockState state = getOriginalBlockState(world, pos);
		;
		state = state.getBlock().getActualState(state, world, pos);
		if (state.getMaterial().isToolNotRequired()) {
			return true;
		}

		ItemStack stack = player.getHeldItemMainhand();
		String tool = block.getHarvestTool(state);
		if (stack.isEmpty() || tool == null) {
			return player.canHarvestBlock(state);
		}

		int toolLevel = stack.getItem().getHarvestLevel(stack, tool, player, state);
		if (toolLevel < 0) {
			return player.canHarvestBlock(state);
		}

		return toolLevel >= block.getHarvestLevel(state);
	}

	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
		return getOriginalBlockState(worldIn, pos).getBlock().getBlockHardness(getOriginalBlockState(worldIn, pos),
				worldIn, pos);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return getOriginalBlockState(source, pos).getBlock().getBoundingBox(getOriginalBlockState(source, pos), source,
				pos);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return getOriginalBlockState(worldIn, pos).getBlock()
				.getCollisionBoundingBox(getOriginalBlockState(worldIn, pos), worldIn, pos);
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
		return getOriginalBlockState(world, pos).getBlock().getExplosionResistance(world, pos, exploder, explosion);
	}

	@Override
	public SoundType getSoundType(IBlockState state, World world, BlockPos pos, Entity entity) {
		return getOriginalBlockState(world, pos).getBlock().getSoundType(getOriginalBlockState(world, pos), world, pos,
				entity);
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return getOriginalBlockState(world, pos).getBlock().canConnectRedstone(getOriginalBlockState(world, pos), world,
				pos, side);
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess worldIn, BlockPos pos, IBlockState state,
			int fortune) {
	}

	public IBlockState getOriginalBlockState(IBlockAccess world, BlockPos pos) {
		ITileCompressed tile = (ITileCompressed) world.getTileEntity(pos);
		IBlockState state;

		if (tile == null)
			return Blocks.STONE.getDefaultState();

		if (tile.getState() == null) {
			state = getOriginalBlockState(ItemCompressed.getOriginal(tile.getItemCompressed()));

		} else if (tile.getState().getBlock() instanceof IBlockCompressed) {
			return Blocks.STONE.getDefaultState();

		} else {
			state = tile.getState();

		}

		return state;

	}

	public int getTime(IBlockAccess world, BlockPos pos) {
		ITileCompressed tile = (ITileCompressed) world.getTileEntity(pos);

		if (tile == null)
			return 0;

		return ItemCompressed.getTime(tile.getItemCompressed());
	}

	public IBlockState getOriginalBlockState(ItemStack item) {
		if (!(item.getItem() instanceof ItemBlock))
			return Blocks.STONE.getDefaultState();

		return ((ItemBlock) item.getItem()).getBlock().getStateFromMeta(item.getMetadata());

	}

	public IBlockState getOriginalBlockState(IBlockState state) {
		if (state instanceof IExtendedBlockState) {
			IExtendedBlockState estate = (IExtendedBlockState) state;

			if (estate.getValue(COMPRESSEDBLOCK_STATE) != null)
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
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World worldIn, BlockPos pos,
			EntityPlayer player) {
		ITileCompressed tileCompressed = (ITileCompressed) worldIn.getTileEntity(pos);
		ItemStack itemCompressed = tileCompressed.getItemCompressed().copy();
		itemCompressed.setCount(1);
		return itemCompressed;
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
		int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, heldItem);
		int silktouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, heldItem);
		ITileCompressed tileCompressed = (ITileCompressed) worldIn.getTileEntity(pos);
		IBlockState iBlockState = tileCompressed.getState();

		if (iBlockState != null && iBlockState.getBlock().canSilkHarvest(worldIn, pos, iBlockState, player)
				&& silktouch == 0) {

			int time = ItemCompressed.getTime(tileCompressed.getItemCompressed());

			NonNullList<ItemStack> itemlist = NonNullList.create();
			iBlockState.getBlock().getDrops(itemlist, worldIn, pos, iBlockState, fortune);

			for (ItemStack itemStack : itemlist) {
				ItemStack compressed = ItemCompressed.createCompressedItem(itemStack, time);
				spawnAsEntity(worldIn, pos, compressed);
			}

		} else {
			ItemStack itemCompressed = tileCompressed.getItemCompressed().copy();
			itemCompressed.setCount(1);
			spawnAsEntity(worldIn, pos, itemCompressed);
		}
	}

	@Override
	public boolean isOpaqueCube(IBlockState iBlockState) {
		return false;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		IProperty<?>[] listedProperties = new IProperty[0];
		IUnlistedProperty<?>[] unlistedProperties = new IUnlistedProperty[] { COMPRESSEDBLOCK_NBT,
				COMPRESSEDBLOCK_STATE };
		return new ExtendedBlockState(this, listedProperties, unlistedProperties);
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		if (state instanceof IExtendedBlockState) {
			IExtendedBlockState retval = (IExtendedBlockState) state;
			TileEntity tileEntity = world.getTileEntity(pos);
			ITileCompressed tileCompressed = (ITileCompressed) tileEntity;

			ItemStack compressed = tileCompressed.getItemCompressed();
			if (compressed != null) {
				NBTTagCompound itemNBT = new NBTTagCompound();
				compressed.writeToNBT(itemNBT);
				retval = retval.withProperty(COMPRESSEDBLOCK_NBT, itemNBT);
			}

			IBlockState blockState = tileCompressed.getState();
			retval = retval.withProperty(COMPRESSEDBLOCK_STATE, blockState);
			return retval;
		}
		return state;
	}

	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		IBlockState original = getOriginalBlockState(world, pos);
		return original.getBlock().doesSideBlockRendering(original, world, pos, face);
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

	private ArrayList<IBlockCompressed> children = new ArrayList<>();
	private IBlockCompressed parent;

	@Override
	public boolean isAvailable(Block item) {
		return true;
	}

	@Override
	public ArrayList<IBlockCompressed> getChildren() {
		return children;
	}

	@Override
	public String getName() {
		return "blockbase";
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
