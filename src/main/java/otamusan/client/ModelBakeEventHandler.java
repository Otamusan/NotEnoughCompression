package otamusan.client;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import otamusan.NotEnoughCompression;
import otamusan.client.ItemCompressed.CompressedModel;

public class ModelBakeEventHandler {

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		IBakedModel itemmodel = event.getModelRegistry().getObject(
				new ModelResourceLocation(NotEnoughCompression.MOD_ID + ":" + "itemcompressed", "inventory"));
		CompressedModel customModel = new CompressedModel(itemmodel);
		event.getModelRegistry().putObject(
				new ModelResourceLocation(NotEnoughCompression.MOD_ID + ":" + "itemcompressed", "inventory"),
				customModel);
	}
}