package warlockjk.compressedBuilding.client.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import warlockjk.compressedBuilding.CompressedBuilding;
import warlockjk.compressedBuilding.inventory.ContainerCompactor;
import warlockjk.compressedBuilding.tileentities.TileEntityCompactor;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class GuiHandler implements IGuiHandler {

	public GuiHandler() {
		NetworkRegistry.instance().registerGuiHandler(CompressedBuilding.instance, this);
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		switch (id) {
		case 0:
			TileEntity te = world.getBlockTileEntity(x, y, z);
			if (te != null && te instanceof TileEntityCompactor) {
				return new ContainerCompactor(player.inventory, (TileEntityCompactor)te);
			}
			break;
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		switch(id) {
		case 0:
			TileEntity te = world.getBlockTileEntity(x, y, z);
			if (te != null && te instanceof TileEntityCompactor) {
				return new GuiCompactor(player.inventory, (TileEntityCompactor)te);
			}
		
			break;
		}
		return null;
	}

}
