package com.omegajak.compressedbuilding.proxies;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy{
	
	public static int renderPass;
	public static int squareTemplateRenderType;
	
	@Override
	public void initSounds() {
		//init all the sounds
	}
	
	public static void setCustomRenderers() {
		squareTemplateRenderType = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new SquareTemplateRenderer());
	}
	
	@Override
	public void initRenderers() {
		DoubleRenderClientProxy.setCustomRenderers();
	}
}
