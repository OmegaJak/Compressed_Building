package com.omegajak.compressedbuilding;

import net.minecraft.creativetab.CreativeTabs;

import com.omegajak.compressedbuilding.blocks.Blocks;
import com.omegajak.compressedbuilding.client.interfaces.GuiHandler;
import com.omegajak.compressedbuilding.config.ConfigHandler;
import com.omegajak.compressedbuilding.creativeTab.CreativeTab;
import com.omegajak.compressedbuilding.items.Items;
import com.omegajak.compressedbuilding.lib.ModInformation;
import com.omegajak.compressedbuilding.proxies.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
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
