package com.omegajak.compressedbuilding.client.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.omegajak.compressedbuilding.tileentities.TileEntityCompactor;

import cpw.mods.fml.common.registry.GameRegistry;

public class SlotCompactor extends Slot {
	
	boolean isInputSlot;
	TileEntityCompactor teInventory = (TileEntityCompactor)this.inventory;
	
	public SlotCompactor(IInventory inventory, int id, int x, int y, boolean isInputSlot) {
		super(inventory, id, x, y);
		this.isInputSlot = isInputSlot;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		if (isInputSlot && stack.getItem() instanceof ItemBlock) {
			return true;
		}
		return false;
	}
	
	@Override
	public void onSlotChanged() {
		if (isInputSlot) {
			teInventory.checkForCompacting();
		}
	}
}
