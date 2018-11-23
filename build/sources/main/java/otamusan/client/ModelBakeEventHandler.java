package otamusan.client;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import otamusan.client.BlockCompressed.BlockCompressedBakedModel;
import otamusan.client.ItemCompressed.CompressedModel;

public class ModelBakeEventHandler {

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		IBakedModel itemmodel = event.getModelRegistry().getObject(ClientProxy.MRItemCompressed);
		CompressedModel customItemModel = new CompressedModel(itemmodel);
		event.getModelRegistry().putObject(ClientProxy.MRItemCompressed, customItemModel);

		IBakedModel itemblockmodel = event.getModelRegistry().getObject(ClientProxy.MRBlockCompressed);
		BlockCompressedBakedModel customBlockModel = new BlockCompressedBakedModel(itemblockmodel);
		event.getModelRegistry().putObject(ClientProxy.MRBlockCompressed, customBlockModel);
	}
}