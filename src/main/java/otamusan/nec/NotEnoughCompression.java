package otamusan.nec;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import otamusan.nec.common.CommonProxy;
import otamusan.nec.common.Lib;

@Mod(modid = Lib.MOD_ID, name = Lib.MOD_NAME, version = Lib.MOD_VERSION, acceptedMinecraftVersions = Lib.MOD_ACCEPTED_MC_VERSIONS, useMetadata = true)

public class NotEnoughCompression {

	@SidedProxy(serverSide = "otamusan.nec.common.CommonProxy", clientSide = "otamusan.nec.client.ClientProxy")
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

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();
	}

}
