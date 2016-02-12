package com.omegajak.compressedbuilding.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import com.omegajak.compressedbuilding.CompressedBuilding;
import com.omegajak.compressedbuilding.network.CompactorMessage;
import com.omegajak.compressedbuilding.tileentities.TileEntityCompactor;

public class RenderCompactor extends TileEntitySpecialRenderer {

	private IModelCustom model;

	public RenderCompactor(IModelCustom model) {
		this.model = model;
	}

	public static final ResourceLocation texture = new ResourceLocation("compressedbuilding", "textures/models/Compactor.png");

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTickTime) {
		GL11.glPushMatrix();

		GL11.glTranslatef((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		
		TileEntityCompactor te = (TileEntityCompactor)tileentity; // Just for quick referencing
		
		//This isn't really necessary at all now, since the implementation of getDescription in TileEntityCompactor
		/*if (te.direction == -1 && !te.hasSentDirectionRequest) { // Hasn't yet been initialized and the message hasn't been sent before
			System.out.println("Sending a message to the server requesting direction");
			//CompressedBuilding.packetPipeline.sendToServer(new PacketCompactor((byte)3, (int)tileentity.xCoord, (int)tileentity.yCoord, (int)tileentity.zCoord));
			CompressedBuilding.network.sendToServer(new CompactorMessage((byte)-1, tileentity.xCoord, tileentity.yCoord, tileentity.zCoord));
			((TileEntityCompactor)tileentity).hasSentDirectionRequest = true; // So we don't keep sending the request every render tick
		}*/

		if (te.direction == 0) {
			GL11.glRotatef(180F, 0F, 1F, 0F);
		}else if (te.direction == 1) {
			GL11.glRotatef(90F, 0F, 1F, 0F);
		}else if (te.direction == 3) {
			GL11.glRotatef(270F, 0F, 1F, 0F);
		}

		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		model.renderAll();

		GL11.glPopMatrix();
	}
}