package com.omegajak.compressedbuilding.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

import com.omegajak.compressedbuilding.client.interfaces.SlotCompactor;
import com.omegajak.compressedbuilding.inventory.ContainerCompactor;
import com.omegajak.compressedbuilding.lib.BlockInfo;
import com.omegajak.compressedbuilding.network.PacketHandler;

public class TileEntityCompactor extends TileEntity implements IInventory {
	
	private ItemStack[] items;
	public ContainerCompactor container;
	int num = 0;
	private boolean isValidInput = false;
	ItemStack anItemStack = null;
	boolean masterShouldDecrement = false;
	public boolean doNotDecrement = false;
	
	public TileEntityCompactor() {
		items = new ItemStack[10];
	}

	@Override
	public int getSizeInventory() {
		return items.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return items[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int count) {
		ItemStack itemStack = getStackInSlot(slot);
		
		if (itemStack != null) {
			if (itemStack.stackSize <= count) {
				setItem(slot, null);
			}else{
				itemStack = itemStack.splitStack(count);
				if (itemStack.stackSize == 0) {
					setItem(slot, null);
                }
			}
		}

		return itemStack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack itemStack = getStackInSlot(slot);
		if (itemStack != null) {
			setItem(slot, null);
		}
		return itemStack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) {

		items[slot] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
		if (!worldObj.isRemote) {
			onInventoryChanged(true, (itemstack == null && slot == 9) || masterShouldDecrement);
		}else if(worldObj.isRemote && slot == 9 && itemstack == null && !this.container.isTransferring && !doNotDecrement) {
			if (determineIfHomogenous() && determineIfFilled()) {
				PacketHandler.sendInterfacePacket((byte)0, items[4].itemID);
			}
		}else if(worldObj.isRemote && slot >= 0 && slot <= 8 && itemstack == null) {
			PacketHandler.sendInterfacePacket((byte)1, 0);
		}
	}

	@Override
	public String getInvName() {
		return "InventoryCompactor";
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return entityplayer.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) <= 64;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		NBTTagList tagList = new NBTTagList();
		
		for (int i = 0; i < getSizeInventory(); i++) {
			if (items[i] != null) {
				NBTTagCompound tagCompound = new NBTTagCompound();
				tagCompound.setByte("Slot", (byte)i);
				items[i].writeToNBT(tagCompound);
				tagList.appendTag(tagCompound);
			}
		}
		
		compound.setTag("Items", tagList);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		//not entirely sure what this does...
		NBTTagList list = compound.getTagList("Items");
		items = new ItemStack[this.getSizeInventory()];
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound tagCompound = (NBTTagCompound)list.tagAt(i);
			int slot = tagCompound.getByte("Slot");
			
			if (slot >= 0 && slot < getSizeInventory()) {
				items[slot] = ItemStack.loadItemStackFromNBT(tagCompound);
			}
		}
	}
	
	public void checkForCompacting(boolean shouldDecrement) {
//		if (!worldObj.isRemote) {
		if (shouldDecrement) {
			decrementInputs();
			shouldDecrement = false;
		}
			System.out.println("checkForCompacting was called for the " + num + " time on the " + sideToString() + " side");
			num++;
			distributeItems();
			if (determineIfHomogenous()) {
				if (determineIfFilled()) {
					System.out.println("Success!");
					isValidInput = true;
					ItemStack itemStack = determineOutput(items[4].itemID);
					System.out.println(itemStack.getUnlocalizedName());
				}
			}
//		}
		if (isValidInput) {
			ItemStack itemStack = determineOutput(items[4].itemID);
//			setInventorySlotContents(9, itemStack);
			setItem(9, itemStack);
			isValidInput = false;
		}
		container.detectAndSendChanges();
	}
	
	private String sideToString() {
		if (worldObj.isRemote) {
			return "client";
		}else{
			return "server";
		}
	}
	
	//Returns true if the item there is actually changed, returns false if nothing was changed
	public boolean setItem(int index, ItemStack itemStack) {
//		if (items[index] == null && itemStack != null && index == 9) {
//			masterShouldDecrement = true;
//		}
		if(items[index] != null && itemStack != null) {//avoiding nullPointerExceptions
			ItemStack oldItemStack = items[index];
			if (oldItemStack.isItemEqual(itemStack) && oldItemStack.stackSize == itemStack.stackSize) {//if the stacks are exactly the same
				return false;
			}
		}
		
		//if it makes it through the above, then set that item slot
		items[index] = itemStack;
		if (!worldObj.isRemote) {//if it's on the server side, tell the container to look for changes and send them to each listener
			onInventoryChanged();
		}
		return true;
	}
	
	@Override
	public void onInventoryChanged() {
		//this is what updates the inventory on the client side when the back-end edits and item, otherwise the GUI must be reloaded to update
		container.detectAndSendChanges();
		super.onInventoryChanged();
	}
	
	public void onInventoryChanged(boolean shouldCheckForCompacting, boolean shouldDecrement) {
		checkForCompacting(shouldDecrement);
		//this is what updates the inventory on the client side when the back-end edits and item, otherwise the GUI must be reloaded to update
		container.detectAndSendChanges();
		super.onInventoryChanged();
	}
	
	//Determines if everything in the compacting grid is the same item
	public boolean determineIfHomogenous() {
		for (int i = 0; i < items.length - 1; i++) {
			if (items[i] != null) {
				int itemID = items[i].itemID;
				for (int k = i; k < items.length - 1; k++) {
					if (items[k] != null && items[k].itemID != itemID) {
						System.out.println("It was not homogenous!");
						return false;
					}
				}
				System.out.println("It was homogenous!");
				return true;
			}
		}
		System.out.println("It was empty!");
		return false;
	}
	
	//Determines if the compacting grid is filled, will only be called if it's homogenous
	public boolean determineIfFilled() {
		for (int i = 0; i < items.length - 1; i++) {
			if (items[i] == null) {
				System.out.println("It was not filled!");
				return false;
			}
		}
		System.out.println("It was filled!");
		return true;
	}
	
	//Equally distributes the items in the crafting grid between each other 
	public void distributeItems() {
	}
	
	public ItemStack determineOutput(int itemID) {
//		System.out.println("This is on the " + sideToString() + " side and in items[4] there is a " + items[4].getDisplayName());
		return new ItemStack(BlockInfo.SQTEMPLATE_ID, 1, itemID);
	}
	
	private void decrementInputs() {
		for (int i = 0; i < items.length - 1; i++) {
	//		items[i].stackSize--;
			decrStackSize(i, 1);
		}
	}
	
	public void recieveInterfaceEvent(byte eventID, int itemID) {
		switch (eventID) {
		case 0:
			setInventorySlotContents(9, null);
			break;
		case 1:
			setItem(9, null);
			break;
		}
	}
}
