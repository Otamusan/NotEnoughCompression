package otamusan.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import otamusan.NotEnoughCompression;

public class TileCompressed extends TileEntity {

	public ItemStack compressedblock;
	public IBlockState state;

	public void setItemCompressed(ItemStack stack) {
		compressedblock = stack.copy();
	}

	public void setBlockState(IBlockState state) {
		this.state = state;
	}

	public ItemStack getItemCompressed() {
		if (compressedblock != null) {
			return compressedblock.copy();
		} else {
			return ItemStack.EMPTY;
		}
	}

	public IBlockState getState() {
		if (state != null) {
			return state;
		} else {
			return Blocks.STONE.getDefaultState();
		}
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		if (compressedblock != null) {

			NBTTagCompound itemnbt = new NBTTagCompound();
			compressedblock.writeToNBT(itemnbt);

			nbt.setTag(NotEnoughCompression.MOD_ID + "_blockcompressed", itemnbt);
		}

		if (state != null) {
			int meta = Block.getStateId(state);
			nbt.setInteger(NotEnoughCompression.MOD_ID + "_originalid", meta);
		}
		return nbt;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey(NotEnoughCompression.MOD_ID + "_blockcompressed")) {
			this.compressedblock = new ItemStack(nbt.getCompoundTag(NotEnoughCompression.MOD_ID + "_blockcompressed"));
		}
		if (nbt.hasKey(NotEnoughCompression.MOD_ID + "_originalid")) {
			this.state = Block.getStateById(nbt.getInteger(NotEnoughCompression.MOD_ID + "_originalid"));
		}
	}

	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
	}

	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		this.readFromNBT(tag);
	}
}
