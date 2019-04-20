package otamusan.nec.client;

import java.awt.Color;
import java.util.ArrayList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.client.FMLClientHandler;
import otamusan.nec.blocks.BlockCompressed;
import otamusan.nec.client.blockcompressed.BlockCompressedBakedModel;
import otamusan.nec.client.blockcompressed.TileSpecialEntityRendererCompressed;
import otamusan.nec.client.itemcompressed.CompressedModel;
import otamusan.nec.common.CommonProxy;
import otamusan.nec.common.Lib;
import otamusan.nec.items.CompressedItemDiversity.ItemCompressed;
import otamusan.nec.tileentity.TileCompressed;
import otamusan.nec.util.ColorUtil;

public class ClientProxy extends CommonProxy {

	// public static final ModelResourceLocation MRItemCompressed = new
	// ModelResourceLocation(
	// Lib.MOD_ID + ":compresseditem", "inventory");

	public static final ArrayList<ModelResourceLocation> MRItemCompresseds = new ArrayList<>();

	public static final ModelResourceLocation MRBlockCompressed = new ModelResourceLocation(
			Lib.MOD_ID + ":compressedblock", "normal");

	public static IBakedModel modelBased;
	public static BlockCompressedBakedModel modelBlockCompressed;
	public static CompressedModel modelItemCompressed;

	@Override
	public void preInit() {
		super.preInit();
		ITEMBASE.ModelRegister();
		ITEMBLOCK.ModelRegister();
	}

	@Override
	public void init() {
		super.init();

		StateMapperBase ignoreState = new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
				return MRBlockCompressed;
			}
		};

		ModelLoader.setCustomStateMapper(CommonProxy.blockCompressed, ignoreState);

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
		}, CommonProxy.blockCompressed);
	}

	public void postInit() {
		super.postInit();

		TileSpecialEntityRendererCompressed.instance.setRendererDispatcher(TileEntityRendererDispatcher.instance);
		TileEntityRendererDispatcher.instance.renderers.put(TileCompressed.class,
				TileSpecialEntityRendererCompressed.instance);
	}

	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().world;
	}

	@Override
	public void registerTileEntity() {
		super.registerTileEntity();
	}

	@Override
	public void registerEventHandlers() {
		super.registerEventHandlers();
		MinecraftForge.EVENT_BUS.register(new TooltipHandler());
		MinecraftForge.EVENT_BUS.register(new ModelBakeEventHandler());
	}
}