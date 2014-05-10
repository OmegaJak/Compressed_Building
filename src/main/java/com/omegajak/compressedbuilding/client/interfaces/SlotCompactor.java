package com.omegajak.compressedbuilding.client.interfaces;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.omegajak.compressedbuilding.blocks.Blocks;
import com.omegajak.compressedbuilding.lib.BlockInfo;
import com.omegajak.compressedbuilding.tileentities.TileEntityCompactor;

public class SlotCompactor extends Slot {
	
	public boolean doNotDecrement = false;
	boolean isInputSlot;
	TileEntityCompactor teInventory = (TileEntityCompactor)this.inventory;
	
	public SlotCompactor(IInventory inventory, int id, int x, int y, boolean isInputSlot) {
		super(inventory, id, x, y);
		this.isInputSlot = isInputSlot;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		if (isInputSlot && stack.getItem() instanceof ItemBlock && !stack.getItem().equals(Item.getItemFromBlock(Blocks.squareTemplate))) {
			return true;
		}
		return false;
	}
	
	@Override
	public void putStack(ItemStack itemStack) {
		super.putStack(itemStack);
	}
}
