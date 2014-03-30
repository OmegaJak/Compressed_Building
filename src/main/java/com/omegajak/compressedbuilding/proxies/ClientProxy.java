package com.omegajak.compressedbuilding.proxies;

import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import com.omegajak.compressedbuilding.client.RenderCompactor;
import com.omegajak.compressedbuilding.client.RenderItemSquareTemplate;
import com.omegajak.compressedbuilding.lib.BlockInfo;
import com.omegajak.compressedbuilding.tileentities.TileEntityCompactor;

import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy{
	
//	public static int renderPass;
	
	@Override
	public void initSounds() {
		//init all the sounds
	}
	
	@Override
	public void initRenderers() {
		RenderItemSquareTemplate sqTemplateRender = new RenderItemSquareTemplate();
		MinecraftForgeClient.registerItemRenderer(BlockInfo.SQTEMPLATE_ID, sqTemplateRender);;
		
		IModelCustom modelCompactor = AdvancedModelLoader.loadModel("/assets/compressedbuilding/models/Compactor.obj");
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCompactor.class, new RenderCompactor(modelCompactor));
		MinecraftForgeClient.registerItemRenderer(BlockInfo.COMPACTOR_ID, new RenderItemCompactor(modelCompactor));
	}
}
