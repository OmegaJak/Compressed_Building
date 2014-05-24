package com.omegajak.compressedbuilding.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import com.omegajak.compressedbuilding.client.render.RenderCompactor;

public class RenderItemCompactor implements IItemRenderer {
	
	private IModelCustom model;
	
	public RenderItemCompactor(IModelCustom model) {
		this.model = model;
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		GL11.glPushMatrix();
		
		switch (type) {
			case EQUIPPED:
				GL11.glTranslatef(0.4F, 1F, 0.6F);
				break;
			case EQUIPPED_FIRST_PERSON:
				GL11.glTranslatef(0F, 0.7F, 0.5F);
				
				GL11.glRotatef(180, 0F, 1F, 0F);
				break;
			default:
		}
		
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(RenderCompactor.texture);
		model.renderAll();
		
		GL11.glPopMatrix();
	}

}
