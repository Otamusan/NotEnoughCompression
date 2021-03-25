package otamusan.nec.common.automaticcompression;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import otamusan.nec.common.config.NECConfig;
import otamusan.nec.items.CompressedItems;
import otamusan.nec.items.CompressedItemDiversity.ItemCompressed;
import otamusan.nec.util.InventoryUtil;

public class AutoCompression {
    public static List<ItemStack> autocompression2(IItemHandler inv) {
        List<ItemStack> remains = new ArrayList<>();
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack source;

            if (ItemCompressed.isCompressedItem(inv.getStackInSlot(i).getItem())) {
                source = ItemCompressed.getOriginal(inv.getStackInSlot(i));
            } else {
                source = inv.getStackInSlot(i).copy();
            }

            CompressedItems manager = new CompressedItems(source);
            if (source.isEmpty())
                continue;
            if (!NECConfig.isCompressible(source.getItem()))
                continue;
            for (int j = i; j < inv.getSlots(); j++) {
                boolean containable = manager.addCompressed(inv.getStackInSlot(j));
                if (containable) {
                    //inv.setStackInSlot(j, ItemStack.EMPTY);
                    inv.extractItem(j, inv.getStackInSlot(j).getCount(), false);
                }
            }
            List<ItemStack> items = manager.getCompressed();
            remains.addAll(InventoryUtil.putStacksInSlots(inv, items));
        }
        return remains;
    }
}
