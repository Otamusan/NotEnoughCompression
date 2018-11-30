package otamusan.client;

import java.awt.Color;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import otamusan.NotEnoughCompression;
import otamusan.blocks.BlockCompressed;
import otamusan.client.blockcompressed.TileSpecialItemRendererCompressed;
import otamusan.client.blockcompressed.TileSpecialRendererCompressed;
import otamusan.common.CommonProxy;
import otamusan.common.NECItems;
import otamusan.tileentity.TileCompressed;

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

		ClientRegistry.bindTileEntitySpecialRenderer(TileCompressed.class, new TileSpecialRendererCompressed());

		StateMapperBase ignoreState = new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
				return MRBlockCompressed;
			}
		};
		ModelLoader.setCustomStateMapper(CommonProxy.blockCompressed, ignoreState);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex) {
				if (stack.getTagCompound() != null) {
					int time = stack.getTagCompound().getInteger(NotEnoughCompression.MOD_ID + "_time") + 1;
					return Color.getHSBColor(0F, 0F, (float) 1.0 / (float) time).getRGB();
				}
				return Color.BLACK.getRGB();
			}
		}, NECItems.itemcompressed);

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

				if (stack.getTagCompound() != null) {
					int time = stack.getTagCompound().getInteger(NotEnoughCompression.MOD_ID + "_time") + 1;
					return Color.getHSBColor(0F, 0F, (float) 1.0 / (float) time).getRGB();
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