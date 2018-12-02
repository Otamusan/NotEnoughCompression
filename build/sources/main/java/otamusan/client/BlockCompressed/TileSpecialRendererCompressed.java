package otamusan.client.blockcompressed;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import otamusan.items.ItemCompressed;
import otamusan.tileentity.TileCompressed;

public class TileSpecialRendererCompressed extends TileEntitySpecialRenderer<TileCompressed> {
	@Override
	public void render(TileCompressed te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		ItemStack itemcompressed = te.getItemCompressed();

		if (itemcompressed == null)
			return;

		if (!itemcompressed.hasTagCompound())
			return;

		ItemStack item = ItemCompressed.getOriginal(itemcompressed);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		// GlStateManager.scale(0.8, 0.8, 0.8);
		GlStateManager.enableLighting();

		// float angle = (te.getWorld().getTotalWorldTime() + partialTicks) /
		// 20.0F * (180F / (float) Math.PI);
		// GlStateManager.rotate(angle, 0.0F, 1.0F, 0.0F);
		Minecraft.getMinecraft().getRenderItem().renderItem(item, ItemCameraTransforms.TransformType.GUI);
		GlStateManager.popMatrix();

	}

	public boolean isGlobalRenderer(TileCompressed te) {
		return true;
	}
}
