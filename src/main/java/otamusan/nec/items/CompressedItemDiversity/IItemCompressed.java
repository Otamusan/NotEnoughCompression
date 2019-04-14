package otamusan.nec.items.CompressedItemDiversity;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public interface IItemCompressed {
	public boolean isAvailable(Item item);

	public ArrayList<IItemCompressed> getChildren();

	public void addChildren(IItemCompressed iItemCompressed);

	public default void registerAll() {

		((Item) this).setRegistryName("compresseditem_" + this.toString());
		((Item) this).setUnlocalizedName("compresseditem");
		ForgeRegistries.ITEMS.register((Item) this);
		for (IItemCompressed child : getChildren()) {
			child.registerAll();
		}
	}

	public default Item getItem(Item original) {
		for (IItemCompressed iItemCompressed : getChildren()) {
			if (iItemCompressed.isAvailable(original)) {
				return iItemCompressed.getItem(original);
			}
		}
		return (Item) this;
	}

	public default ArrayList<Item> getCompressedItems() {
		ArrayList<Item> items = new ArrayList<>();
		items.add((Item) this);
		for (IItemCompressed iItemCompressed : getChildren()) {
			items.addAll(iItemCompressed.getCompressedItems());
		}
		return items;
	}
}
