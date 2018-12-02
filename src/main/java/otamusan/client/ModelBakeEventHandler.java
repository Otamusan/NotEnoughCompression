package otamusan.client;

import java.util.ArrayList;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import otamusan.client.blockcompressed.BlockCompressedBakedModel;
import otamusan.client.itemcompressed.CompressedItemOverrideList;
import otamusan.client.itemcompressed.CompressedModel;

public class ModelBakeEventHandler {

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		IBakedModel itemmodel = event.getModelRegistry().getObject(ClientProxy.MRItemCompressed);
		CompressedModel customItemModel = new CompressedModel(itemmodel,
				new CompressedItemOverrideList(new ArrayList<>()));
		event.getModelRegistry().putObject(ClientProxy.MRItemCompressed, customItemModel);

		IBakedModel itemblockmodel = event.getModelRegistry().getObject(ClientProxy.MRBlockCompressed);
		BlockCompressedBakedModel customBlockModel = new BlockCompressedBakedModel(itemblockmodel);
		event.getModelRegistry().putObject(ClientProxy.MRBlockCompressed, customBlockModel);
	}
}