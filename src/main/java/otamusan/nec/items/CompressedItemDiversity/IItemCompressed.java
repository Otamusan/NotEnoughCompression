package otamusan.nec.items.CompressedItemDiversity;

import java.util.ArrayList;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import otamusan.nec.client.ClientProxy;
import otamusan.nec.client.blockcompressed.TileSpecialItemRendererCompressed;
import otamusan.nec.common.Lib;

public interface IItemCompressed {
	public boolean isAvailable(Item item);

	public ArrayList<IItemCompressed> getChildren();

	public default void addChildren(IItemCompressed iItemCompressed) {
		getChildren().add(iItemCompressed);
		iItemCompressed.setParent(this);
		((Item) iItemCompressed).setTileEntityItemStackRenderer(TileSpecialItemRendererCompressed.instance);

		((Item) iItemCompressed).setRegistryName(Lib.MOD_ID + ":compresseditem" + iItemCompressed.getNameTreed());
		((Item) iItemCompressed).setUnlocalizedName("compresseditem");
		ForgeRegistries.ITEMS.register((Item) iItemCompressed);
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

	public default ArrayList<IItemCompressed> getCompressedItemCompressed() {
		ArrayList<IItemCompressed> items = new ArrayList<>();
		items.add(this);
		for (IItemCompressed iItemCompressed : getChildren()) {
			items.addAll(iItemCompressed.getCompressedItemCompressed());
		}
		return items;
	}

	public String getName();

	public IItemCompressed getParent();

	public void setParent(IItemCompressed iItemCompressed);

	public default String getNameTreed() {

		if (getParent() == null) {
			return "_" + getName();
		}

		return getParent().getNameTreed() + "_" + getName();
	}

	@SideOnly(Side.CLIENT)
	public default void modelRegister() {
		ModelResourceLocation modelResourceLocation = new ModelResourceLocation(
				Lib.MOD_ID + ":compresseditem" + getNameTreed(), "inventory");
		ClientProxy.MRItemCompresseds.add(modelResourceLocation);
		ModelLoader.setCustomModelResourceLocation((Item) this, 0, modelResourceLocation);
	}

}
