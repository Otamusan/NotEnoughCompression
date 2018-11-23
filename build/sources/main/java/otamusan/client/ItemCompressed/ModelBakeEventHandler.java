package otamusan.client.ItemCompressed;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import otamusan.NotEnoughCompression;

public class ModelBakeEventHandler {

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		IBakedModel model = event.getModelRegistry().getObject(
				new ModelResourceLocation(NotEnoughCompression.MOD_ID + ":" + "itemcompressed", "inventory"));
		CompressedModel customModel = new CompressedModel(model);
		event.getModelRegistry().putObject(
				new ModelResourceLocation(NotEnoughCompression.MOD_ID + ":" + "itemcompressed", "inventory"),
				customModel);
	}
}