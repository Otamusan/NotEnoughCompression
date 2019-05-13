package otamusan.nec.blocks;

import java.awt.Color;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import otamusan.nec.client.ClientProxy;
import otamusan.nec.common.Lib;
import otamusan.nec.items.CompressedItemDiversity.ItemCompressed;
import otamusan.nec.util.ColorUtil;

public interface IBlockCompressed {
	public boolean isAvailable(Block item);

	public ArrayList<IBlockCompressed> getChildren();

	public default void addChildren(IBlockCompressed iBlockCompressed) {
		getChildren().add(iBlockCompressed);
		iBlockCompressed.setParent(this);
		((Block) iBlockCompressed).setRegistryName(Lib.MOD_ID + ":compresseditem" + iBlockCompressed.getNameTreed());
		((Block) iBlockCompressed).setUnlocalizedName("compresseditem");
		ForgeRegistries.BLOCKS.register((Block) iBlockCompressed);
	}

	public default Block getBlock(Block original) {
		for (IBlockCompressed iBlockCompressed : getChildren()) {
			if (iBlockCompressed.isAvailable(original)) {
				return iBlockCompressed.getBlock(original);
			}
		}
		return (Block) this;
	}

	public default ArrayList<Block> getCompressedBlocks() {
		ArrayList<Block> blocks = new ArrayList<>();
		blocks.add((Block) this);
		for (IBlockCompressed iBlockCompressed : getChildren()) {
			blocks.addAll(iBlockCompressed.getCompressedBlocks());
		}
		return blocks;
	}

	public default ArrayList<IBlockCompressed> getCompressedBlockCompressed() {
		ArrayList<IBlockCompressed> blocks = new ArrayList<>();
		blocks.add(this);
		for (IBlockCompressed iBlockCompressed : getChildren()) {
			blocks.addAll(iBlockCompressed.getCompressedBlockCompressed());
		}
		return blocks;
	}

	public String getName();

	public IBlockCompressed getParent();

	public void setParent(IBlockCompressed iBlockCompressed);

	public default String getNameTreed() {

		if (getParent() == null) {
			return "_" + getName();
		}

		return getParent().getNameTreed() + "_" + getName();
	}

	@SideOnly(Side.CLIENT)
	public default void preModelRegister() {
		ModelResourceLocation MRBlockCompressed = new ModelResourceLocation(
				Lib.MOD_ID + ":compresseditem" + this.getNameTreed(), "normal");

		ClientProxy.MRBlockCompresseds.add(MRBlockCompressed);
		StateMapperBase ignoreState = new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
				return MRBlockCompressed;
			}
		};

		ModelLoader.setCustomStateMapper((Block) this, ignoreState);
	}

	@SideOnly(Side.CLIENT)
	public default void modelRegister() {
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor() {
			@Override
			public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
				if (!(state instanceof IExtendedBlockState))
					return -1;
				IExtendedBlockState eState = (IExtendedBlockState) state;

				NBTTagCompound itemnbt = eState.getValue(BlockCompressed.COMPRESSEDBLOCK_NBT);

				if (itemnbt == null)
					return -1;

				ItemStack stack = new ItemStack(itemnbt);

				int time = ItemCompressed.getTime(stack);

				if (time > 0) {
					IBlockState originalState = eState.getValue(BlockCompressed.COMPRESSEDBLOCK_STATE);

					int k;

					int oritintindex = tintIndex - 100;
					if (oritintindex == -1) {
						k = ColorUtil.getCompressedColor(time + 1).getRGB();
					} else {
						int colorInt = Minecraft.getMinecraft().getBlockColors().colorMultiplier(originalState, worldIn,
								pos, oritintindex);
						k = ColorUtil.getCompressedColor(new Color(colorInt), time + 1).getRGB();
					}
					return k;
				}
				return -1;
			}
		}, (Block) this);
	}
}
