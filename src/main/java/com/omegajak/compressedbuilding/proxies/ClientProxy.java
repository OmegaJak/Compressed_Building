package com.omegajak.compressedbuilding.proxies;

import net.minecraftforge.client.MinecraftForgeClient;

import com.omegajak.compressedbuilding.client.RenderItemSquareTemplate;
import com.omegajak.compressedbuilding.lib.BlockInfo;

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
	}
}
