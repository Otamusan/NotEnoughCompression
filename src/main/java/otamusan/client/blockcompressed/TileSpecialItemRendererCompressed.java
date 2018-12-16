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
import otamusan.NotEnoughCompression;
import otamusan.tileentity.TileCompressed;

public class TileSpecialItemRendererCompressed extends TileEntityItemStackRenderer {
	private final TileCompressed compressed = new TileCompressed();
	public static TileSpecialItemRendererCompressed instance;

	@Override
	public void renderByItem(ItemStack itemStackIn) {
		this.renderByItem(itemStackIn, 1.0F);
	}

	@Override
	public void renderByItem(ItemStack p_192838_1_, float partialTicks) {

		int time = p_192838_1_.getTagCompound().getInteger(NotEnoughCompression.MOD_ID + "_time") + 1;
		Color color = Color.getHSBColor(0F, 0F, (float) 1.0 / (float) time);

		renderModel(Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(p_192838_1_, null, null), -1,
				p_192838_1_, color);

	}

	private void renderModel(IBakedModel model, int color, ItemStack stack, Color color2) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.ITEM);

		for (EnumFacing enumfacing : EnumFacing.values()) {
			renderQuads(bufferbuilder, model.getQuads((IBlockState) null, enumfacing, 0L), color, stack);
		}

		this.renderQuads(bufferbuilder, model.getQuads((IBlockState) null, (EnumFacing) null, 0L), color, stack);

		for (int i = 0; i < bufferbuilder.getVertexCount(); i++) {
			bufferbuilder.putColorMultiplier(color2.getRed() / 255f, color2.getGreen() / 255f, color2.getGreen() / 255f,
					i);
		}

		tessellator.draw();
	}

	private void renderQuads(BufferBuilder renderer, List<BakedQuad> quads, int color, ItemStack stack) {
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

	static {
		instance = new TileSpecialItemRendererCompressed();
	}
}
