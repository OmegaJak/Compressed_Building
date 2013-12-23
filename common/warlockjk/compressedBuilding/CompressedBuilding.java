package warlockjk.compressedBuilding;

import net.minecraft.creativetab.CreativeTabs;
import warlockjk.compressedBuilding.blocks.Blocks;
import warlockjk.compressedBuilding.client.interfaces.GuiHandler;
import warlockjk.compressedBuilding.config.ConfigHandler;
import warlockjk.compressedBuilding.creativeTab.CreativeTab;
import warlockjk.compressedBuilding.items.Items;
import warlockjk.compressedBuilding.lib.ModInformation;
import warlockjk.compressedBuilding.network.PacketHandler;
import warlockjk.compressedBuilding.proxies.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = ModInformation.ID, name = ModInformation.NAME, version = ModInformation.VERSION)
@NetworkMod(channels = {ModInformation.CHANNEL}, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)
public class CompressedBuilding {
	
	public static CreativeTabs tabCompressedBuilding;
	
	@Instance(ModInformation.ID)
	public static CompressedBuilding instance;
	
	@SidedProxy(clientSide = "warlockjk.compressedBuilding.proxies.ClientProxy", serverSide = "warlockjk.compressedBuilding.proxies.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {//Doesn't have to be named preInit
		ConfigHandler.init(event.getSuggestedConfigurationFile());
		tabCompressedBuilding = new CreativeTab("Compressed Building");
		Items.init();
		Blocks.init();
		
		
		proxy.initSounds();
		proxy.initRenderers();
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event) {
		Blocks.addNames();
		
		Items.registerRecipes();
		
		Blocks.registerTileEntities();
		
		new GuiHandler();
	}
	
	@EventHandler
	public void modsLoaded(FMLPostInitializationEvent event) {
		
	}
}
