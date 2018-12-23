package otamusan.client.blockcompressed;

import java.awt.Color;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import otamusan.items.ItemCompressed;
import otamusan.tileentity.TileCompressed;

public class TileSpecialItemRendererCompressed extends TileEntityItemStackRenderer {
	private final TileCompressed compressed = new TileCompressed();
	public static TileSpecialItemRendererCompressed instance;

	@Override
	public void renderByItem(ItemStack itemStackIn) {
		this.renderByItem(itemStackIn, 1.0F);
	}

	@Override
	public void renderByItem(ItemStack stack, float partialTicks) {

		int time = ItemCompressed.getTime(stack) + 1;
		ItemStack original = ItemCompressed.getOriginal(stack);

		// int intColor =
		// Minecraft.getMinecraft().getItemColors().colorMultiplier(original,
		// 0);
		// Color color = new Color(intColor);
		// float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(),
		// color.getBlue(), new float[3]);
		// color = Color.getHSBColor(hsb[0], hsb[1], (float) 1.0 / (float)
		// time);

		renderModel(Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(original, null, null), original,
				time);

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
				Color color = new Color(intColor);
				float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), new float[3]);
				color = Color.getHSBColor(hsb[0], hsb[1], (float) 1.0 / (float) time);
				k = color.getRGB();

				if (EntityRenderer.anaglyphEnable) {
					k = TextureUtil.anaglyphColor(k);
				}

				k = k | -16777216;

			} else {
				Color color = Color.getHSBColor(0f, 0f, (float) 1.0 / (float) time);
				k = color.getRGB();
			}

			net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, k);
		}
	}

	static {
		instance = new TileSpecialItemRendererCompressed();
	}
}
