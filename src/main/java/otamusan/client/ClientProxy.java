package otamusan.client;

import java.awt.Color;

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
import otamusan.NotEnoughCompression;
import otamusan.blocks.BlockCompressed;
import otamusan.client.blockcompressed.BlockCompressedBakedModel;
import otamusan.client.blockcompressed.TileSpecialEntityRendererCompressed;
import otamusan.client.blockcompressed.TileSpecialItemRendererCompressed;
import otamusan.client.itemcompressed.CompressedModel;
import otamusan.common.CommonProxy;
import otamusan.items.ItemCompressed;
import otamusan.tileentity.TileCompressed;

public class ClientProxy extends CommonProxy {

	public static final ModelResourceLocation MRItemCompressed = new ModelResourceLocation(
			NotEnoughCompression.MOD_ID + ":compresseditem", "inventory");
	public static final ModelResourceLocation MRBlockCompressed = new ModelResourceLocation(
			NotEnoughCompression.MOD_ID + ":compressedblock", "normal");

	public static IBakedModel modelBased;
	public static BlockCompressedBakedModel modelBlockCompressed;
	public static CompressedModel modelItemCompressed;

	@Override
	public void preInit() {
		super.preInit();

		ModelLoader.setCustomModelResourceLocation(CommonProxy.itemCompressed, 0, MRItemCompressed);
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
						k = ItemCompressed.getCompressedColor(time + 1).getRGB();
					} else {
						int colorInt = Minecraft.getMinecraft().getBlockColors().colorMultiplier(originalState, worldIn,
								pos, oritintindex);
						k = ItemCompressed.getCompressedColor(new Color(colorInt), time + 1).getRGB();
					}
					return k;
				}
				return -1;
			}
		}, CommonProxy.blockCompressed);

	}

	public void postInit() {
		CommonProxy.itemCompressed.setTileEntityItemStackRenderer(TileSpecialItemRendererCompressed.instance);
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