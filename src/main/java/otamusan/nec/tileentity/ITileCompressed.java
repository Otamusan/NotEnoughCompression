package otamusan.nec.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public interface ITileCompressed {
	public void setItemCompressed(ItemStack stack);

	public void setBlockState(IBlockState state);

	public ItemStack getItemCompressed();

	public IBlockState getState();
}
