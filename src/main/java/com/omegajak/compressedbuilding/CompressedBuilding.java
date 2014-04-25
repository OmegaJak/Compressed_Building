package com.omegajak.compressedbuilding;

import net.minecraft.creativetab.CreativeTabs;

import com.omegajak.compressedbuilding.blocks.Blocks;
import com.omegajak.compressedbuilding.client.interfaces.GuiHandler;
import com.omegajak.compressedbuilding.config.ConfigHandler;
import com.omegajak.compressedbuilding.creativeTab.CreativeTab;
import com.omegajak.compressedbuilding.items.Items;
import com.omegajak.compressedbuilding.lib.ModInformation;
import com.omegajak.compressedbuilding.network.PacketPipeline;
import com.omegajak.compressedbuilding.proxies.CommonProxy;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
//import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = ModInformation.ID, name = ModInformation.NAME, version = ModInformation.VERSION)
//@NetworkMod(channels = {ModInformation.CHANNEL}, clientSideRequired = true, serverSideRequired = true, packetHandler = PacketHandler.class)
public class CompressedBuilding {
	
	public static CreativeTabs tabCompressedBuilding;
	
	@Instance(ModInformation.ID)
	public static CompressedBuilding instance;
	
	@SidedProxy(clientSide = "com.omegajak.compressedbuilding.proxies.ClientProxy", serverSide = "com.omegajak.compressedbuilding.proxies.CommonProxy")
	public static CommonProxy proxy;
	
	//the packet pipeline
	public static final PacketPipeline packetPipeline = new PacketPipeline();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {//Doesn't have to be named preInit
		ConfigHandler.init(event.getSuggestedConfigurationFile());
		tabCompressedBuilding = new CreativeTab("Compressed Building");
		
		Blocks.init();
		Blocks.addNames();
		Blocks.registerTileEntities();
		
		Items.init();
		Items.registerRecipes();
		
		new GuiHandler();
		
		proxy.initSounds();
		proxy.initRenderers();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		packetPipeline.postInitialise();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		packetPipeline.initialise();
	}
	
	@EventHandler
	public void modsLoaded(FMLPostInitializationEvent event) {
		
	}
}
