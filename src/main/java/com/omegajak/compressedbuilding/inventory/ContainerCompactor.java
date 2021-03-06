package com.omegajak.compressedbuilding.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.omegajak.compressedbuilding.blocks.Blocks;
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
    
    public TileEntityCompactor getCompactor() {
    	return (TileEntityCompactor)compactor;
    }
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int i) {
		
		ItemStack newItemStack = null;
		
		if (getCompactor().getWorldObj().isRemote) {
			this.compactor.isTransferring = true;
		}
		Slot slot = getSlot(i);
		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			newItemStack = stack.copy();
			
			if (stack.getItem().equals(Item.getItemFromBlock(Blocks.squareTemplate)) && i > 9)
				return null;
			
			if (i <= 9) {
				if (!mergeItemStack(stack, 10, 46, false)) {
					return null;
				}
			}else if(!(stack.getItem() instanceof ItemBlock) || !mergeItemStack(stack, 1, compactor.getSizeInventory(), false)) {
				return null;
			}
			
			if (stack.stackSize == 0) {
				slot.putStack(null);
			}else{
				slot.onSlotChanged();
			}
			
		}
		return newItemStack;
	}
	
	@Override
	public void putStackInSlot(int slotNumber, ItemStack itemStack) {
		super.putStackInSlot(slotNumber, itemStack);
	}

}
