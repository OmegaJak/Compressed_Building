package warlockjk.compressedBuilding.client.interfaces;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class SlotCompactor extends Slot {
	
	boolean isInputSlot;
	
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
	
	public void onSlotChange(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		
	}
}
