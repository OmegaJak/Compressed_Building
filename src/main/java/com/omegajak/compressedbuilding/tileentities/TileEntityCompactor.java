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
	public boolean isDecrementing;
    public boolean isTransferring = false;//set to true when user shift-clicks, when its true setInterfacePacket(0,0) cant be called
    public int transferPass = 0;//used to keep track of how many times setInvSlotContents has been called after someone shift-clicks, so isTransferring can be false again
    public int smallestIn = 0;//the number of items in the smallest input stack
	
	
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
		
		if (!worldObj.isRemote) {//if its on the server side
			onInventoryChanged(true, (itemstack == null && slot == 9));
		}else if(worldObj.isRemote && slot == 9 && itemstack == null && !this.isTransferring) {//if youre simply removing the output, no shift clicking though
			if (determineIfHomogenous() && determineIfFilled()) {//always good to check
				PacketHandler.sendInterfacePacket((byte)0, items[4].itemID);//tells the server to set output to null and do normal updating stuff
																			//including decrementing
			}
		}else if(worldObj.isRemote && slot >= 0 && slot <= 8 && itemstack == null) {//if you take an input out
			PacketHandler.sendInterfacePacket((byte)1, 0);//let the server know
		}
		if(worldObj.isRemote && this.isTransferring && slot != 9) {
			if (transferPass == 0) {//need this because it is reset each time
				findSmallestInput();
			}
			if (transferPass == smallestIn * 9) {//this method is called once for each item it decrements
				this.transferPass = 0;//reset
				this.isTransferring = false;//allow future actions on client side to call sendInterfacePacket
			}else{
				this.transferPass++;//the counter
			}
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
	
	//set the field smallestIn to the input found to have the smallest stack size, used for incrementing transferPass
	public void findSmallestInput() {
		if (determineIfHomogenous() && determineIfFilled()) {
			smallestIn = 66;//any stack should be smaller than this, its just a starting point
			for (int i = 0; i < items.length - 1; i++) {
				if (items[i].stackSize < smallestIn) {
					smallestIn = items[i].stackSize;
				}
			}
		}
	}
	
	//this determines whether the inputs are valid and whether an output should be set
	public void checkForCompacting(boolean shouldDecrement) {
		if (shouldDecrement) {//decrement upon removing the output
			decrementInputs();
			shouldDecrement = false;
		}
		distributeItems();//non-functional at the moment
		if (determineIfHomogenous() && determineIfFilled()) {
			isValidInput = true;//it was determined that the inputs are valid
		}
		if (isValidInput) {
			ItemStack itemStack = determineOutput(items[4].itemID,items[4].getItemDamage());//itemstack with stackSize of 1, id of squareTemplate, and damage of the inputs
			setItem(9, itemStack);//don't want it to call checkForCompacting, though we do want the updates to be sent
			isValidInput = false;//might no longer be valid
		}
		container.detectAndSendChanges();
	}
	
	//Returns true if the item there is actually changed, returns false if nothing was changed
	public boolean setItem(int index, ItemStack itemStack) {
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
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	//Determines if the compacting grid is filled, will only be called if it's homogenous
	public boolean determineIfFilled() {
		for (int i = 0; i < items.length - 1; i++) {
			if (items[i] == null) {
				return false;
			}
		}
		return true;
	}
	
	//Equally distributes the items in the crafting grid between each other 
	public void distributeItems() {
	}
	
	public ItemStack determineOutput(int itemID, int itemMetadata) {
		int newItemDamage = 0;
		int newID = itemID << 8;
		newItemDamage = newID | itemMetadata;
		return new ItemStack(BlockInfo.SQTEMPLATE_ID, 1, newItemDamage);
	}
	
	//well... it goes through each of the input stacks and decrements it
	private void decrementInputs() {
		this.isDecrementing = true;
		for (int i = 0; i < items.length - 1; i++) {
			decrStackSize(i, 1);
		}
		this.isDecrementing = false;
	}
	
	//server side end of sendInterfaceEvent
	public void recieveInterfaceEvent(byte eventID, int itemID) {
		switch (eventID) {
		case 0://set the output to null and call checkForCompacting, decrementInputs
			setInventorySlotContents(9, null);
			break;
		case 1://avoids calling checkForCompacting
			setItem(9, null);
			break;
		}
	}

	public ItemStack getItemInSlot(int slotNumber) {
		return  items[slotNumber];
	}
}
