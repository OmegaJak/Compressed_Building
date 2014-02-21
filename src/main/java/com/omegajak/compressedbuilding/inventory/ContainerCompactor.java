package com.omegajak.compressedbuilding.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.omegajak.compressedbuilding.client.interfaces.SlotCompactor;
import com.omegajak.compressedbuilding.tileentities.TileEntityCompactor;

public class ContainerCompactor extends Container {

    private TileEntityCompactor compactor;

    public ContainerCompactor(InventoryPlayer inventoryPlayer, TileEntityCompactor compactor) {
    	this.compactor = compactor;
    	compactor.container = this;
        int i;
        int k;
        
        //add the output slot
        this.addSlotToContainer(new SlotCompactor(this.compactor, 9, 124, 35, false));
        
        //add the 'crafting grid'
        for (i = 0; i < 3; i++)
        {
            for (k = 0; k < 3; k++)
            {
                this.addSlotToContainer(new SlotCompactor(this.compactor, k + i * 3, 30 + k * 18, 17 + i * 18, true));
            }
        }
        
        //add the player's inventory
        for (i = 0; i < 3; ++i)
        {
            for (k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new Slot(inventoryPlayer, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
            }
        }
        
        //player's hotbar
        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }
    }
    
    @Override
	public boolean canInteractWith(EntityPlayer entityPlayer) {
		return compactor.isUseableByPlayer(entityPlayer);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int i) {
		return null;
	}

}
