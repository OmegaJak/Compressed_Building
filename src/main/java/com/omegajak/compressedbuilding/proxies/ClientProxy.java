package com.omegajak.compressedbuilding.proxies;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import com.omegajak.compressedbuilding.blocks.Blocks;
import com.omegajak.compressedbuilding.client.RenderCompactor;
import com.omegajak.compressedbuilding.client.RenderItemCompactor;
import com.omegajak.compressedbuilding.client.RenderItemSquareTemplate;
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
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(Blocks.squareTemplate), sqTemplateRender);
		
		BlockInfo.COMPACTOR_RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
		IModelCustom modelCompactor = AdvancedModelLoader.loadModel(new ResourceLocation(BlockInfo.TEXTURE_LOCATION, BlockInfo.COMPACTOR_MODEL_RENDER_LOCATION));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCompactor.class, new RenderCompactor(modelCompactor));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(Blocks.compactor), new RenderItemCompactor(modelCompactor));
	}
}
