package otamusan.client.BlockCompressed;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedPropertyCompressedBlockState implements IUnlistedProperty<IBlockState> {
	@Override
	public String getName() {
		return "UnlistedPropertyCompressedBlockState";
	}

	@Override
	public boolean isValid(IBlockState value) {
		return true;
	}

	@Override
	public Class<IBlockState> getType() {
		return IBlockState.class;
	}

	@Override
	public String valueToString(IBlockState value) {
		return value.toString();
	}
}