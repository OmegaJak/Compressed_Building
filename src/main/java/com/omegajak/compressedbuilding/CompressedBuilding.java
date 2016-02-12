package com.omegajak.compressedbuilding;

import net.minecraft.creativetab.CreativeTabs;

import com.omegajak.compressedbuilding.blocks.Blocks;
import com.omegajak.compressedbuilding.client.interfaces.GuiHandler;
import com.omegajak.compressedbuilding.config.ConfigHandler;
import com.omegajak.compressedbuilding.creativeTab.CreativeTab;
import com.omegajak.compressedbuilding.items.Items;
import com.omegajak.compressedbuilding.lib.ModInformation;
import com.omegajak.compressedbuilding.network.CompactorMessage;
import com.omegajak.compressedbuilding.proxies.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
//import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = ModInformation.ID, name = ModInformation.NAME, version = ModInformation.VERSION)
//@NetworkMod(channels = {ModInformation.CHANNEL}, clientSideRequired = true, serverSideRequired = true, packetHandler = PacketHandler.class)
public class CompressedBuilding {
	
	public static CreativeTabs tabCompressedBuilding;
	public static SimpleNetworkWrapper network;
	
	@Instance(ModInformation.ID)
	public static CompressedBuilding instance;
	
	@SidedProxy(clientSide = "com.omegajak.compressedbuilding.proxies.ClientProxy", serverSide = "com.omegajak.compressedbuilding.proxies.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {//Doesn't have to be named preInit
		ConfigHandler.init(event.getSuggestedConfigurationFile());
		tabCompressedBuilding = new CreativeTab("Compressed Building");
		
		Blocks.init();
		Blocks.registerTileEntities();
		
		Items.init();
		Items.registerRecipes();
		
		new GuiHandler();
		
		proxy.initSounds();
		proxy.initRenderers();
		
		network = NetworkRegistry.INSTANCE.newSimpleChannel("CompressedBuilding");
		network.registerMessage(CompactorMessage.Handler.class, CompactorMessage.class, 0, Side.SERVER); // Need to do this for each message on each side it'll be used on
		//network.registerMessage(CompactorMessage.Handler.class, CompactorMessage.class, 0, Side.CLIENT);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
	}
	
	@EventHandler
	public void modsLoaded(FMLPostInitializationEvent event) {
		
	}
}
