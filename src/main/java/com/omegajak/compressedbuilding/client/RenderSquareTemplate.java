package com.omegajak.compressedbuilding.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import com.omegajak.compressedbuilding.proxies.ClientProxy;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderSquareTemplate implements ISimpleBlockRenderingHandler{

	private int id;
	public RenderSquareTemplate() {
		id = RenderingRegistry.getNextAvailableRenderId();
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		if (ClientProxy.renderPass == 0) {
			block.setBlockBoundsForItemRender();
			renderer.setRenderBoundsFromBlock(block);
			
			GL11.glPushMatrix();
			GL11.glRotatef(90, 0, 1, 0);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			
			Tessellator tessellator = Tessellator.instance;
			
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1.0F, 0.0F);
			renderer.renderFaceYNeg(block, 0, 0, 0, block.getIcon(0, metadata));
			tessellator.draw();
			
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			renderer.renderFaceYPos(block, 0, 0, 0, block.getIcon(1, metadata));
			tessellator.draw();
			
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			renderer.renderFaceZNeg(block, 0, 0, 0, block.getIcon(2, metadata));
			tessellator.draw();
			
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			renderer.renderFaceZPos(block, 0, 0, 0, block.getIcon(3, metadata));
			tessellator.draw();
			
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			renderer.renderFaceXNeg(block, 0, 0, 0, block.getIcon(4, metadata));
			tessellator.draw();
			
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			renderer.renderFaceXPos(block, 0, 0, 0, block.getIcon(5, metadata));
			tessellator.draw();
			
			GL11.glPopMatrix();
		} else {
			
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		renderer.renderStandardBlock(block, x, y, z);
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return true;
	}

	@Override
	public int getRenderId() {
		return id;
	}

}
