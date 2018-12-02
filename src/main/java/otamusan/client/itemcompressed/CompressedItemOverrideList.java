package otamusan.client.itemcompressed;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import otamusan.NotEnoughCompression;

public class CompressedItemOverrideList extends ItemOverrideList {
	public CompressedItemOverrideList(List<ItemOverride> override) {
		super(override);
	}

	@Override
	public IBakedModel handleItemState(IBakedModel model, ItemStack stack, World world, EntityLivingBase entity) {

		if (stack == null)
			return Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(new ItemStack(Blocks.BARRIER),
					world, entity);
		if (stack.getTagCompound() == null)
			return Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(new ItemStack(Blocks.BARRIER),
					world, entity);
		if (!stack.getTagCompound().hasKey(NotEnoughCompression.MOD_ID + "_itemstack"))
			return Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(new ItemStack(Blocks.BARRIER),
					world, entity);
		IBakedModel newmodel = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(
				new ItemStack(stack.getTagCompound().getCompoundTag(NotEnoughCompression.MOD_ID + "_itemstack")), world,
				entity);

		IBakedModel bakedModel = new CompressedModel(newmodel, ItemOverrideList.NONE);
		return bakedModel;
	}

}