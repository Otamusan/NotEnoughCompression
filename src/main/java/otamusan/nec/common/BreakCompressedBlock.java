package otamusan.nec.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import otamusan.nec.blocks.CompressedBlockDiversity.IBlockCompressed;
import otamusan.nec.items.CompressedItemDiversity.ItemCompressed;
import otamusan.nec.tileentity.ITileCompressed;

public class BreakCompressedBlock {
    @SubscribeEvent
    public void onBlockBreaked(BreakEvent event) {
        World world = event.getWorld();

        if (!((world.getBlockState(event.getPos()).getBlock()) instanceof IBlockCompressed))
            return;

        ITileCompressed compressed = (ITileCompressed) world.getTileEntity(event.getPos());
        int time = ItemCompressed.getTime(compressed.getItemCompressed());
        EntityPlayer player = event.getPlayer();
        ItemStack stack = player.getHeldItem(player.getActiveHand());
        if (compressed.isNatural()) {
            for (int i = 0; i < Math.pow(8, time) - 1; i++) {
                stack.onBlockDestroyed(world, world.getBlockState(event.getPos()), event.getPos(), player);
            }
        }

    }
}
