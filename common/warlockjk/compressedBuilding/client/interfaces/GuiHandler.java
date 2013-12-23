package warlockjk.compressedBuilding.client.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import warlockjk.compressedBuilding.CompressedBuilding;
import warlockjk.compressedBuilding.inventory.ContainerCompactor;
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
			return new ContainerCompactor(player.inventory, world, x, y, z);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		switch(id) {
		case 0:
			return new GuiCompactor(player.inventory, world, x, y, z);
		}
		return null;
	}

}
