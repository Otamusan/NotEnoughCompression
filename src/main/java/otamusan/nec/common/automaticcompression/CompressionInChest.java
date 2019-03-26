package otamusan.nec.common.automaticcompression;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import otamusan.nec.common.config.NECConfig;

public class CompressionInChest {
	@SubscribeEvent
	public void onUpdate(WorldTickEvent event) {
		Map<BlockPos, Item> candidates = Maps.newHashMap();

		for (Entity entity : event.world.loadedEntityList) {
			if (entity instanceof EntityItemFrame) {
				EntityItemFrame frame = (EntityItemFrame) entity;
				Item displayedItem = frame.getDisplayedItem().getItem();
				if (NECConfig.isCompressionCatalyst(displayedItem)) {
					BlockPos pos = entity.getPosition().offset(frame.getAdjustedHorizontalFacing(), -1).down();
					candidates.put(pos, displayedItem);
				}

			}
		}

		for (Entry<BlockPos, Item> entry : candidates.entrySet()) {
			BlockPos pos = entry.getKey();
			Item type = entry.getValue();

			TileEntity tile = event.world.getTileEntity(pos);
			if (tile instanceof IInventory) {
				IInventory inventory = (IInventory) tile;
				if (NECConfig.isCompressionCatalyst(type)) {
					List<ItemStack> remains = AutoCompression.autocompression2(inventory);

					for (ItemStack itemStack : remains) {
						Block.spawnAsEntity(event.world, pos.add(0.5, 0.5, 0.5), itemStack);
					}

				}
			}
		}
	}
}
