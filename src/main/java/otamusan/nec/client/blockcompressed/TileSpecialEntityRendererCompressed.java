package otamusan.nec.client.blockcompressed;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import otamusan.nec.items.CompressedItemDiversity.ItemCompressed;
import otamusan.nec.tileentity.TileCompressed;
import otamusan.nec.util.CustomRenderHelper;

public class TileSpecialEntityRendererCompressed extends TileEntitySpecialRenderer<TileCompressed> {
	public static TileSpecialEntityRendererCompressed instance = new TileSpecialEntityRendererCompressed();

	@Override
	public void renderTileEntityFast(TileCompressed te, double x, double y, double z, float partialTicks,
			int destroyStage, float alpha, BufferBuilder buffer) {
		render(te, x, y, z, partialTicks, destroyStage, alpha);
	}

	@Override
	public void render(TileCompressed te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		IBlockState state_child = te.getState();
		if (state_child!=null&&state_child.getRenderType()!=EnumBlockRenderType.MODEL) {
			Block block_child = state_child.getBlock();
			TileEntity te_child = block_child.createTileEntity(getWorld(), state_child);
			if (te_child != null) {
				int time = ItemCompressed.getTime(te.compressedblock) + 1;
				GlStateManager.pushAttrib();
				CustomRenderHelper.percentAllLights(1.f / time);
				rendererDispatcher.render(te_child, x, y, z, partialTicks, destroyStage, alpha);
				GlStateManager.popAttrib();
			}
		}
	}
}
