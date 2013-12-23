package warlockjk.compressedBuilding.client.interfaces;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import warlockjk.compressedBuilding.inventory.ContainerCompactor;
import warlockjk.compressedBuilding.lib.BlockInfo;

public class GuiCompactor extends GuiContainer {

	public GuiCompactor(InventoryPlayer invplayer, World world, int x, int y, int z) {
		super(new ContainerCompactor(invplayer, world, x, y, z));
	}
	
	private static final ResourceLocation texture = new ResourceLocation(BlockInfo.TEXTURE_LOCATION, "textures/gui/compactor.png");
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1, 1, 1, 1);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

}
