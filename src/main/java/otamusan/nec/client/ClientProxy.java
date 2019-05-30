package otamusan.nec.client;

import java.util.ArrayList;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import otamusan.nec.client.blockcompressed.TileSpecialEntityRendererCompressed;
import otamusan.nec.client.blockcompressed.TileSpecialItemRendererCompressed;
import otamusan.nec.common.CommonProxy;
import otamusan.nec.tileentity.TileCompressed;

public class ClientProxy extends CommonProxy {

	// public static final ModelResourceLocation MRItemCompressed = new
	// ModelResourceLocation(
	// Lib.MOD_ID + ":compresseditem", "inventory");

	public static final ArrayList<ModelResourceLocation> MRItemCompresseds = new ArrayList<>();

	public static final ArrayList<ModelResourceLocation> MRBlockCompresseds = new ArrayList<>();

	public static IBakedModel modelBased;
	//public static BlockCompressedBakedModel modelBlockCompressed;
	//public static CompressedModel modelItemCompressed;

	@Override
	public void preInit() {
		super.preInit();
		((Item) ITEMBASE).setTileEntityItemStackRenderer(TileSpecialItemRendererCompressed.instance);
		ITEMBASE.modelRegister();
		ITEMBLOCK.modelRegister();
		BLOCKBASE.preModelRegister();
		//BLOCKFURNECE.preModelRegister();
	}

	@Override
	public void init() {
		BLOCKBASE.modelRegister();
		//BLOCKFURNECE.modelRegister();
		super.init();
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