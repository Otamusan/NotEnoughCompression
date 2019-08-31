package otamusan.nec.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import otamusan.nec.common.Lib;

public class TileCompressed extends TileEntity implements ITileCompressed {

	public ItemStack compressedblock;
	public IBlockState state;
	public boolean isNatural = false;

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

			nbt.setTag(Lib.MOD_ID + "_blockcompressed", itemnbt);
		}

		if (state != null) {
			int meta = Block.getStateId(state);
			nbt.setInteger(Lib.MOD_ID + "_originalid", meta);
		}

		nbt.setBoolean(Lib.MOD_ID + "_isnatural", isNatural);

		return nbt;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey(Lib.MOD_ID + "_blockcompressed")) {
			this.compressedblock = new ItemStack(nbt.getCompoundTag(Lib.MOD_ID + "_blockcompressed"));
		}
		if (nbt.hasKey(Lib.MOD_ID + "_originalid")) {
			this.state = Block.getStateById(nbt.getInteger(Lib.MOD_ID + "_originalid"));
		}

		if (nbt.hasKey(Lib.MOD_ID + "_isnatural")) {
			this.isNatural = nbt.getBoolean(Lib.MOD_ID + "_isnatural");
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

	@Override
	public void setNatural(boolean isnatural) {
		this.isNatural = isnatural;
	}

	@Override
	public boolean isNatural() {
		return isNatural;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}
}
