package otamusan.client.blockcompressed;

import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
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
		compressed.setItemCompressed(p_192838_1_);
		TileEntityRendererDispatcher.instance.render(compressed, partialTicks, 0);
		System.out.println(234234);
	}

	static {
		instance = new TileSpecialItemRendererCompressed();
	}
}
