package otamusan.client;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import otamusan.NotEnoughCompression;
import otamusan.client.BlockCompressed.TileSpecialRendererCompressed;
import otamusan.common.CommonProxy;
import otamusan.common.NECItems;
import otamusan.tileentity.TileCompressed;

public class ClientProxy extends CommonProxy {
	@Override
	public void preInit() {
		super.preInit();

		ModelLoader.setCustomModelResourceLocation(CommonProxy.itemBlockCompressed, 0,
				new ModelResourceLocation(NotEnoughCompression.MOD_ID + ":blockcompression", "inventory"));

		ModelLoader.setCustomModelResourceLocation(NECItems.itemcompressed, 0,
				new ModelResourceLocation(NotEnoughCompression.MOD_ID + ":" + "itemcompressed#inventory"));

	}

	@Override
	public void init() {
		super.init();

		ClientRegistry.bindTileEntitySpecialRenderer(TileCompressed.class, new TileSpecialRendererCompressed());
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