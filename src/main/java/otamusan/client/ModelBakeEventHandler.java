package otamusan.client;

import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import otamusan.client.blockcompressed.BlockCompressedBakedModel;
import otamusan.client.itemcompressed.CompressedItemOverrideList;
import otamusan.client.itemcompressed.CompressedModel;

public class ModelBakeEventHandler {

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		ClientProxy.modelBased = event.getModelManager().getBlockModelShapes().getModelForState(Blocks.STONE.getDefaultState());

		ClientProxy.modelItemCompressed = new CompressedModel(ClientProxy.modelBased, new CompressedItemOverrideList(new ArrayList<>()));
		event.getModelRegistry().putObject(ClientProxy.MRItemCompressed, ClientProxy.modelItemCompressed);

		ClientProxy.modelBlockCompressed = new BlockCompressedBakedModel(ClientProxy.modelBased);
		event.getModelRegistry().putObject(ClientProxy.MRBlockCompressed, ClientProxy.modelBlockCompressed);
	}
}