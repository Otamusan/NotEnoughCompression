package otamusan.client.blockcompressed;

import java.awt.Color;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import otamusan.items.ItemCompressed;
import otamusan.util.CustomRenderHelper;

public class TileSpecialItemRendererCompressed extends TileEntityItemStackRenderer {
	public static TileSpecialItemRendererCompressed instance = new TileSpecialItemRendererCompressed();

	@Override
	public void renderByItem(ItemStack itemStackIn) {
		this.renderByItem(itemStackIn, 1.0F);
	}

	@Override
	public void renderByItem(ItemStack stack, float partialTicks) {

		int time = ItemCompressed.getTime(stack) + 1;
		ItemStack original = ItemCompressed.getOriginal(stack);
		IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(original, null, null);
		if (!model.isBuiltInRenderer()) {
			renderModel(model, original, time);
		} else {
			GlStateManager.pushAttrib();
			CustomRenderHelper.percentAllLights(1.f / time);
			original.getItem().getTileEntityItemStackRenderer().renderByItem(original);
			GlStateManager.popAttrib();
		}

	}

	private void renderModel(IBakedModel model, ItemStack stack, int time) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.ITEM);

		for (EnumFacing enumfacing : EnumFacing.values()) {
			renderQuads(bufferbuilder, model.getQuads((IBlockState) null, enumfacing, 0L), stack, time);
		}

		this.renderQuads(bufferbuilder, model.getQuads((IBlockState) null, (EnumFacing) null, 0L), stack, time);

		tessellator.draw();
	}

	private void renderQuads(BufferBuilder renderer, List<BakedQuad> quads, ItemStack stack, int time) {
		int i = 0;

		for (int j = quads.size(); i < j; ++i) {
			BakedQuad bakedquad = quads.get(i);
			int k;

			if (bakedquad.hasTintIndex()) {

				int intColor = Minecraft.getMinecraft().getItemColors().colorMultiplier(stack,
						bakedquad.getTintIndex());
				k = ItemCompressed.getCompressedColor(new Color(intColor), time).getRGB();

				if (EntityRenderer.anaglyphEnable) {
					k = TextureUtil.anaglyphColor(k);
				}

				k = k | -16777216;

			} else {
				k = ItemCompressed.getCompressedColor(time).getRGB();
			}

			net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, k);
		}
	}
}
