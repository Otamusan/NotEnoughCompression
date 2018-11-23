package otamusan;

import java.awt.Color;
import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import otamusan.common.CommonProxy;
import otamusan.register.ItemRegister;

@Mod(modid = NotEnoughCompression.MOD_ID, name = NotEnoughCompression.MOD_NAME, version = NotEnoughCompression.MOD_VERSION, acceptedMinecraftVersions = NotEnoughCompression.MOD_ACCEPTED_MC_VERSIONS, useMetadata = true)

public class NotEnoughCompression {
	public static final String MOD_ID = "notenoughcompression";
	public static final String MOD_NAME = "NotEnoughCompression";
	public static final String MOD_VERSION = "0.0.1";
	public static final String MOD_ACCEPTED_MC_VERSIONS = "[1.12.2]";
	@SidedProxy(clientSide = "otamusan.client.ClientProxy", serverSide = "otamusan.common.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		Configuration cfg = new Configuration(new File("config/NotEnoughCompression.cfg"));
		try {
			cfg.load();
		} finally {
			cfg.save();
		}
		ItemRegister.init(event.getSide());
		proxy.registerTileEntity();
		proxy.registerEventHandlers();

	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex) {

				if (stack.getTagCompound() != null) {
					int time = stack.getTagCompound().getInteger(MOD_ID + "_time") + 1;
					return Color.getHSBColor(0F, 0F, (float) 1.0 / (float) time).getRGB();
				}
				return Color.BLACK.getRGB();
			}
		}, ItemRegister.itemcompressed);

	}
}
