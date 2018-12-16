package otamusan.client;

import java.awt.Color;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.client.FMLClientHandler;
import otamusan.NotEnoughCompression;
import otamusan.blocks.BlockCompressed;
import otamusan.client.blockcompressed.TileSpecialItemRendererCompressed;
import otamusan.common.CommonProxy;
import otamusan.common.NECItems;
import otamusan.items.ItemCompressed;

public class ClientProxy extends CommonProxy {

	public static final ModelResourceLocation MRItemCompressed = new ModelResourceLocation(
			NotEnoughCompression.MOD_ID + ":compresseditem", "inventory");
	public static final ModelResourceLocation MRBlockCompressed = new ModelResourceLocation(
			NotEnoughCompression.MOD_ID + ":compressedblock", "normal");

	@Override
	public void preInit() {
		super.preInit();

		ModelLoader.setCustomModelResourceLocation(NECItems.itemcompressed, 0, MRItemCompressed);

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

				if (stack.getTagCompound()!=null) {
					ItemStack original = ItemCompressed.getOriginal(stack);
					Color color = Color.BLACK;
					int time = ItemCompressed.getTime(stack)+1;

					if (original.getItem() instanceof ItemBlock) {
						Block block = ((ItemBlock) original.getItem()).getBlock();
						ImmutableSet<IProperty<?>> props = eState.getProperties().keySet();
						ImmutableSet<IUnlistedProperty<?>> unlistedProps = eState.getUnlistedProperties().keySet();

						IBlockState instate = new ExtendedBlockState(
								block,
								props.toArray(new IProperty<?>[props.size()]),
								unlistedProps.toArray(new IUnlistedProperty<?>[unlistedProps.size()])).getBaseState();
						int intColor = Minecraft.getMinecraft().getBlockColors().colorMultiplier(instate, worldIn, pos, tintIndex);
						color = new Color(intColor);
					}

					float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), new float[3]);
					return Color.getHSBColor(hsb[0], hsb[1], (float) 1.0/(float) time).getRGB();
				}
				return -1;
			}
		}, CommonProxy.blockCompressed);

	}

	public void postInit() {
		NECItems.itemcompressed.setTileEntityItemStackRenderer(TileSpecialItemRendererCompressed.instance);
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