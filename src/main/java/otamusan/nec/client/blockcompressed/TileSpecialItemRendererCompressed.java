package otamusan.nec.client.blockcompressed;

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
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import otamusan.nec.items.ItemCompressed;
import otamusan.nec.util.ColorUtil;
import otamusan.nec.util.CustomRenderHelper;

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
			if (original.hasEffect()) {
				renderEffect(model, stack);
			}
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
				k = ColorUtil.getCompressedColor(new Color(intColor), time).getRGB();

				if (EntityRenderer.anaglyphEnable) {
					k = TextureUtil.anaglyphColor(k);
				}

				k = k | -16777216;

			} else {
				k = ColorUtil.getCompressedColor(time).getRGB();
			}

			net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, k);
		}
	}

	private void renderEffect(IBakedModel model, ItemStack item) {
		GlStateManager.depthMask(false);
		GlStateManager.depthFunc(514);
		GlStateManager.disableLighting();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
		Minecraft.getMinecraft().getTextureManager()
				.bindTexture(new ResourceLocation("textures/misc/enchanted_item_glint.png"));
		GlStateManager.matrixMode(5890);
		GlStateManager.pushMatrix();
		GlStateManager.scale(8.0F, 8.0F, 8.0F);
		float f = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
		GlStateManager.translate(f, 0.0F, 0.0F);
		GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
		this.vanillaRenderModel(model, -8372020, item);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.scale(8.0F, 8.0F, 8.0F);
		float f1 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
		GlStateManager.translate(-f1, 0.0F, 0.0F);
		GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
		this.vanillaRenderModel(model, -8372020, item);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(5888);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableLighting();
		GlStateManager.depthFunc(515);
		GlStateManager.depthMask(true);
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	}

	private void vanillaRenderModel(IBakedModel model, int color, ItemStack stack) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.ITEM);

		for (EnumFacing enumfacing : EnumFacing.values()) {
			this.VanillaRenderQuads(bufferbuilder, model.getQuads((IBlockState) null, enumfacing, 0L), color, stack);
		}

		this.VanillaRenderQuads(bufferbuilder, model.getQuads((IBlockState) null, (EnumFacing) null, 0L), color, stack);
		tessellator.draw();
	}

	private void VanillaRenderQuads(BufferBuilder renderer, List<BakedQuad> quads, int color, ItemStack stack) {
		boolean flag = color == -1 && !stack.isEmpty();
		int i = 0;

		for (int j = quads.size(); i < j; ++i) {
			BakedQuad bakedquad = quads.get(i);
			int k = color;

			if (flag && bakedquad.hasTintIndex()) {
				k = Minecraft.getMinecraft().getItemColors().colorMultiplier(stack, bakedquad.getTintIndex());

				if (EntityRenderer.anaglyphEnable) {
					k = TextureUtil.anaglyphColor(k);
				}

				k = k | -16777216;
			}

			net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, k);
		}
	}

}
