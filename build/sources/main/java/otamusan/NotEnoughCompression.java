package otamusan;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import otamusan.common.CommonProxy;

@Mod(modid = NotEnoughCompression.MOD_ID, name = NotEnoughCompression.MOD_NAME, version = NotEnoughCompression.MOD_VERSION, acceptedMinecraftVersions = NotEnoughCompression.MOD_ACCEPTED_MC_VERSIONS, useMetadata = true)

public class NotEnoughCompression {
	public static final String MOD_ID = "notenoughcompression";
	public static final String MOD_NAME = "NotEnoughCompression";
	public static final String MOD_VERSION = "0.0.1";
	public static final String MOD_ACCEPTED_MC_VERSIONS = "[1.12.2]";
	@SidedProxy(serverSide = "otamusan.common.CommonProxy", clientSide = "otamusan.client.ClientProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		Configuration cfg = new Configuration(new File("config/NotEnoughCompression.cfg"));
		try {
			cfg.load();
		} finally {
			cfg.save();
		}
		proxy.registerTileEntity();
		proxy.registerEventHandlers();
		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();

	}
}
