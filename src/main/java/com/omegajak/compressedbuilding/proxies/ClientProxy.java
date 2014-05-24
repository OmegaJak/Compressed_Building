package com.omegajak.compressedbuilding.proxies;

import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import com.omegajak.compressedbuilding.client.render.RenderCompactor;
import com.omegajak.compressedbuilding.client.render.RenderItemCompactor;
import com.omegajak.compressedbuilding.client.render.RenderItemSquareTemplate;
import com.omegajak.compressedbuilding.lib.BlockInfo;
import com.omegajak.compressedbuilding.tileentities.TileEntityCompactor;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

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
		
		BlockInfo.COMPACTOR_RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
		IModelCustom modelCompactor = AdvancedModelLoader.loadModel(BlockInfo.COMPACTOR_MODEL_RENDER_LOCATION);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCompactor.class, new RenderCompactor(modelCompactor));
		MinecraftForgeClient.registerItemRenderer(BlockInfo.COMPACTOR_ID, new RenderItemCompactor(modelCompactor));
	}
}
