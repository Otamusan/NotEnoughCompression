package otamusan.nec.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import otamusan.nec.common.config.NECConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SupplyItems {
    @CapabilityInject(Provider.Ref.class)
    public static Capability<Provider.Ref> ISFIRST;


    public static void capRegister() {
        CapabilityManager.INSTANCE.register(Provider.Ref.class, new Capability.IStorage<Provider.Ref>() {

            @Override
            public NBTBase writeNBT(Capability<Provider.Ref> capability, Provider.Ref instance, EnumFacing side) {
                NBTTagCompound nbtBase = new NBTTagCompound();
                nbtBase.setBoolean(Lib.MOD_ID + "isfirst", instance.t);
                return nbtBase;
            }

            @Override
            public void readNBT(Capability<Provider.Ref> capability, Provider.Ref ref, EnumFacing enumFacing, NBTBase nbtBase) {
                NBTTagCompound tagCompound = (NBTTagCompound) nbtBase;
                ref.t = tagCompound.getBoolean(Lib.MOD_ID + "isfirst");
            }

        }, () -> {
            throw new UnsupportedOperationException();
        });
    }

    public static class Provider implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {
        public Ref isfirst = new Ref(true);

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing enumFacing) {
            return capability == ISFIRST;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing enumFacing) {
            return (T) isfirst;
        }

        public static class Ref {
            public Boolean t;

            public Ref(Boolean t) {
                this.t = t;
            }
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setBoolean(Lib.MOD_ID + "isfirst", isfirst.t);
            return nbt;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbtTagCompound) {
            isfirst.t = nbtTagCompound.getBoolean(Lib.MOD_ID + "isfirst");
        }

    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!NECConfig.CONFIG_TYPES.world.isReplaceVanillaRecipe)
            return;

        Provider.Ref ref= event.player.getCapability(ISFIRST,null);
        if (!ref.t)
            return;
        event.player.inventory.addItemStackToInventory(new ItemStack(Blocks.PISTON));
        event.player.inventory.addItemStackToInventory(new ItemStack(Blocks.CRAFTING_TABLE));
        ref.t = false;
    }

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event){
        if (!(event.getObject() instanceof EntityPlayer)) return;
        event.addCapability(new ResourceLocation("neccap"), new Provider());
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            if (event.getOriginal().hasCapability(ISFIRST, null)) {
                Provider.Ref oldStore = event.getOriginal().getCapability(ISFIRST, null);
                Provider.Ref newStore = event.getEntityPlayer().getCapability(ISFIRST,null);
                newStore.t= oldStore.t;
            }
        }
    }
}
