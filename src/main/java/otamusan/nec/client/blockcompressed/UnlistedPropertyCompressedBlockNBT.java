package otamusan.nec.client.blockcompressed;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedPropertyCompressedBlockNBT implements IUnlistedProperty<NBTTagCompound> {
    @Override
    public String getName() {
        return "UnlistedPropertyCompressedBlockNBT";
    }

    @Override
    public boolean isValid(NBTTagCompound value) {
        return true;
    }

    @Override
    public Class<NBTTagCompound> getType() {
        return NBTTagCompound.class;
    }

    @Override
    public String valueToString(NBTTagCompound value) {
        return value.toString();
    }
}