package otamusan.common.automaticcompression;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import otamusan.items.ItemCompressed;
import otamusan.util.InventoryUtil;

public class AutoCompression {
	public static void autocompression(IInventory inv) {

		IBakedModel ibakedmodel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
				.getItemModel(ItemCompressed.createCompressedItem(new ItemStack(Items.APPLE), 4));

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.getCount() >= 8) {
				ItemStack compressed = ItemCompressed.createCompressedItem(stack);
				if (InventoryUtil.putStackInSlots(inv, compressed).isEmpty()) {
					stack.setCount(stack.getCount() - 8);
				}
			}
		}
	}
}
