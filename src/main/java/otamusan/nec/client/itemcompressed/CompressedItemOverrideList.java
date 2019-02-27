package otamusan.nec.client.itemcompressed;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import otamusan.nec.items.ItemCompressed;

public class CompressedItemOverrideList extends ItemOverrideList {
	public CompressedItemOverrideList(List<ItemOverride> override) {
		super(override);
	}

	@Override
	public IBakedModel handleItemState(IBakedModel model, ItemStack stack, World world, EntityLivingBase entity) {
		if (stack!=null) {
			ItemStack itemStack = ItemCompressed.getOriginal(stack);
			if (!itemStack.isEmpty()) {
				IBakedModel newmodel = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(itemStack, world, entity);
				return new CompressedModel(newmodel, ItemOverrideList.NONE);
			}
		}

		return Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(new ItemStack(Blocks.BARRIER),
				world, entity);
	}
}