package otamusan.nec.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import otamusan.nec.common.CommonProxy;
import otamusan.nec.items.CompressedItemDiversity.ItemCompressed;

public class Decompression extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        ItemStack base = getBase(inv);
        return getItemAmount(inv) == 1 && !base.isEmpty();
    }

    private int getItemAmount(InventoryCrafting inv) {
        int a = 0;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            if (!inv.getStackInSlot(i).isEmpty())
                a++;
        }
        return a;
    }

    private ItemStack getBase(InventoryCrafting inv) {
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack current = inv.getStackInSlot(i);
            if (!current.isEmpty()) {
                if (ItemCompressed.isCompressedItem(current.getItem()))
                    return current.copy();
                else
                    return ItemStack.EMPTY;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(CommonProxy.ITEMBASE);
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return ItemCompressed.createUncompressedItem(getBase(inv));
    }

    @Override
    public boolean isDynamic() {
        return true;
    }
}
