package otamusan.nec.client;

import java.util.ArrayList;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import otamusan.nec.client.blockcompressed.BlockCompressedBakedModel;
import otamusan.nec.client.itemcompressed.CompressedItemOverrideList;
import otamusan.nec.client.itemcompressed.CompressedModel;

public class ModelBakeEventHandler {

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		ClientProxy.modelBased = event.getModelManager().getBlockModelShapes()
				.getModelForState(Blocks.STONE.getDefaultState());

		for (ModelResourceLocation modelResourceLocation : ClientProxy.MRItemCompresseds) {
			event.getModelRegistry().putObject(modelResourceLocation,
					new CompressedModel(ClientProxy.modelBased, new CompressedItemOverrideList(new ArrayList<>())));

		}
		for (ModelResourceLocation modelResourceLocation : ClientProxy.MRBlockCompresseds) {
			event.getModelRegistry().putObject(modelResourceLocation,
					new BlockCompressedBakedModel(ClientProxy.modelBased));
		}

	}
}