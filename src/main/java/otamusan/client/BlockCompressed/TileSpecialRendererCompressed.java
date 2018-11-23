package otamusan.client.BlockCompressed;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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

		Block block = ((ItemBlock) ItemCompressed.getOriginal(itemcompressed).getItem()).getBlock();
		IBlockState iBlockState = block.getStateFromMeta(itemcompressed.getMetadata());

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableBlend();
		GlStateManager.disableCull();
		// bufferbuilder = new BufferBuilder(262144);

		IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(iBlockState);

		// Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(te.getWorld(),
		// model,
		// iBlockState, te.getPos(), bufferbuilder, true);
		bufferbuilder.setTranslation(x - MathHelper.floor(x), y - MathHelper.floor(y), z - MathHelper.floor(z));

		Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlock(iBlockState, new BlockPos(x, y, z),
				te.getWorld(), bufferbuilder);

		// Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModelSmooth(te.getWorld(),
		// model, iBlockState, new BlockPos(x, y, z), bufferbuilder, true, 0);
		bufferbuilder.setTranslation(0, 0, 0);
		tessellator.draw();

		TileEntitySpecialRenderer<TileEntity> tileentityspecialrenderer = TileEntityRendererDispatcher.instance
				.<TileEntity>getRenderer(block.createTileEntity(te.getWorld(), iBlockState));

		if (tileentityspecialrenderer != null) {
			tileentityspecialrenderer.render(block.createTileEntity(te.getWorld(), iBlockState), x, y, z, partialTicks,
					destroyStage, alpha);
		}
	}

	public boolean isGlobalRenderer(TileCompressed te) {
		return true;
	}
}
