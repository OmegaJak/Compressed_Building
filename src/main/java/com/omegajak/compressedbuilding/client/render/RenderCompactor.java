package com.omegajak.compressedbuilding.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import com.omegajak.compressedbuilding.CompressedBuilding;
import com.omegajak.compressedbuilding.network.PacketCompactor;
import com.omegajak.compressedbuilding.tileentities.TileEntityCompactor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCompactor extends TileEntitySpecialRenderer {

	private IModelCustom model;
	public int direction;
	private final RenderItem innerItemRender;

	public RenderCompactor(IModelCustom model) {
		this.model = model;
		
		//As is quite common, I used Pahimar's code from the Glass Bell to do render the inner item, it's always a great resource
		innerItemRender = new RenderItem() {
			@Override
			public boolean shouldBob() {
				return false;
			}
		};
		
		innerItemRender.setRenderManager(RenderManager.instance);
	}

	public static final ResourceLocation texture = new ResourceLocation("compressedbuilding", "textures/models/Compactor.png");

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTickTime) {
		GL11.glPushMatrix();

		GL11.glTranslatef((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		
		this.direction = ((TileEntityCompactor)tileentity).direction;
		if (direction == -1) {
			//This sends a message to the server which in turn returns the message to the client, updating the client side on world load
			CompressedBuilding.packetPipeline.sendToServer(new PacketCompactor((byte)3, (int)tileentity.xCoord, (int)tileentity.yCoord, (int)tileentity.zCoord));
		}

		if (direction == 0) {//Rotate it based on its direction
			GL11.glRotatef(180F, 0F, 1F, 0F);
		}else if (direction == 1) {
			GL11.glRotatef(90F, 0F, 1F, 0F);
		}else if (direction == 3) {
			GL11.glRotatef(270F, 0F, 1F, 0F);
		}
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		model.renderAll();

		GL11.glPopMatrix();
		
		//Render the item on the inside
		GL11.glPushMatrix();
		
		if (((TileEntityCompactor)tileentity).determineIfHomogenous()) {
			EntityItem innerItemEntity = new EntityItem(tileentity.getWorldObj());
			innerItemEntity.hoverStart = 0.0F;
			
			int filled = -1;
			if (((TileEntityCompactor)tileentity).getItemInSlot(((TileEntityCompactor)tileentity).getSizeInventory() - 1) == null) {
				for (int i=0; i<((TileEntityCompactor)tileentity).getSizeInventory();i++) {//find the first filled slot
					if (((TileEntityCompactor)tileentity).getItemInSlot(i) != null) {
						filled = i;
						break;
					}
				}
			}else{
				filled = ((TileEntityCompactor)tileentity).getSizeInventory() - 1;
			}
			innerItemEntity.setEntityItemStack(((TileEntityCompactor)tileentity).getItemInSlot(filled));
			
			orientationBasedTranslation(direction, x, y, z);
			
			innerItemRender.doRender(innerItemEntity, 0, 0, 0, 0, 0);
		}
		
		GL11.glPopMatrix();
	}

	private void orientationBasedTranslation(int direction, double x, double y, double z) {
		switch (direction) {
			case 0:
				GL11.glTranslatef((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.3F);
				break;
			case 1:
				GL11.glTranslatef((float)x + 0.7F, (float)y + 0.5F, (float)z + 0.5F);
				break;
			case 2:
				GL11.glTranslatef((float)x + 0.5F, (float)y + 0.5F, (float)z + .7F);
				break;
			case 3:
				GL11.glTranslatef((float)x + 0.3F, (float)y + 0.5F, (float)z + 0.5F);
				break;
		}
	}
}