package otamusan.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import otamusan.NotEnoughCompression;

public class TileCompressed extends TileEntity {

	public ItemStack compressedblock;

	public void setItemCompressed(ItemStack stack) {
		compressedblock = stack.copy();
	}

	public ItemStack getItemCompressed() {
		if (compressedblock != null) {
			return compressedblock.copy();
		} else {
			return null;
		}
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		if (compressedblock == null)
			return nbt;

		NBTTagCompound itemnbt = new NBTTagCompound();
		compressedblock.writeToNBT(itemnbt);

		nbt.setTag(NotEnoughCompression.MOD_ID + "_blockcompressed", itemnbt);
		return nbt;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (!nbt.hasKey(NotEnoughCompression.MOD_ID + "_blockcompressed"))
			return;
		this.compressedblock = new ItemStack(nbt.getCompoundTag(NotEnoughCompression.MOD_ID + "_blockcompressed"));
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
