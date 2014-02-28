package com.omegajak.compressedbuilding.proxies;

import net.minecraftforge.client.MinecraftForgeClient;

import com.omegajak.compressedbuilding.client.RenderSquareTemplate;
import com.omegajak.compressedbuilding.lib.BlockInfo;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy{
	
	public static int renderPass;
	
	@Override
	public void initSounds() {
		//init all the sounds
	}
	
	@Override
	public void initRenderers() {
		RenderSquareTemplate sqTemplateRender = new RenderSquareTemplate();
		BlockInfo.SQTEMPLATE_RENDER_ID = sqTemplateRender.getRenderId();
		RenderingRegistry.registerBlockHandler(BlockInfo.SQTEMPLATE_RENDER_ID, sqTemplateRender);;
	}
}
