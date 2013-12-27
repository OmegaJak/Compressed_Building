package warlockjk.compressedBuilding.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.world.World;
import warlockjk.compressedBuilding.client.interfaces.SlotCompactor;

public class ContainerCompactor extends Container {

	/** The crafting matrix inventory (3x3). */
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    public IInventory craftResult = new InventoryCraftResult();
    @SuppressWarnings("unused")
	private World worldObj;
    private int posX;
    private int posY;
    private int posZ;

    public ContainerCompactor(InventoryPlayer inventoryPlayer, World world, int x, int y, int z)
    {
        this.worldObj = world;
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.addSlotToContainer(new SlotCrafting(inventoryPlayer.player, this.craftMatrix, this.craftResult, 0, 124, 35));
        int i;
        int k;

        for (i = 0; i < 3; i++)
        {
            for (k = 0; k < 3; k++)
            {
                this.addSlotToContainer(new SlotCompactor(this.craftMatrix, k + i * 3, 30 + k * 18, 17 + i * 18));
            }
        }

        for (i = 0; i < 3; ++i)
        {
            for (k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new Slot(inventoryPlayer, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }

        this.onCraftMatrixChanged(this.craftMatrix);
    }

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return entityplayer.getDistanceSq(posX + 0.5, posY + 0.5, posZ + 0.5) <= 64;
	}

}
