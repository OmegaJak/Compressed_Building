package com.omegajak.compressedbuilding.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
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
        
        this.addSlotToContainer(new SlotCompactor(this.compactor, 9, 124, 35, false));

        for (i = 0; i < 3; i++)
        {
            for (k = 0; k < 3; k++)
            {
                this.addSlotToContainer(new SlotCompactor(this.compactor, k + i * 3, 30 + k * 18, 17 + i * 18, true));
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
    }
    
    @Override
	public boolean canInteractWith(EntityPlayer entityPlayer) {
		return compactor.isUseableByPlayer(entityPlayer);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int i) {
		Slot slot = getSlot(i);
		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			ItemStack result = stack.copy();
			
			if (i >= 36) {
				if (!mergeItemStack(stack, 0, 36, false)) {
					return null;
				}
			}else if(!(stack.getItem() instanceof ItemBlock) || !mergeItemStack(stack, 36, 36 + compactor.getSizeInventory() - 1, false)) {
				return null;
			}
			
			if (stack.stackSize == 0) {
				slot.putStack(null);
			}else{
				slot.onSlotChanged();
			}
			
			slot.onPickupFromSlot(player, stack);
			
			return result;
		}
		
		return null;
	}

}
