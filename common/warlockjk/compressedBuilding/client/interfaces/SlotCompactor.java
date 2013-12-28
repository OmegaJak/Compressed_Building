package warlockjk.compressedBuilding.client.interfaces;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class SlotCompactor extends Slot {
	
	boolean isInput;
	
	public SlotCompactor(IInventory inventory, int id, int x, int y, boolean isInput) {
		super(inventory, id, x, y);
		this.isInput = isInput;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		if (isInput && stack.getItem() instanceof ItemBlock) {
			return true;
		}
		return false;
	}
}
