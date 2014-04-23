package com.omegajak.compressedbuilding.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

import com.omegajak.compressedbuilding.inventory.ContainerCompactor;
import com.omegajak.compressedbuilding.lib.BlockInfo;

public class TileEntityCompactor extends TileEntity implements ISidedInventory {
	
	private ItemStack[] items;
	public ContainerCompactor container;
	int num = 0;
	private boolean isValidInput = false;
	public boolean isDecrementing;
    public boolean isTransferring = false;//set to true when user shift-clicks, when its true setInterfacePacket(0,0) cant be called
    public int transferPass = 0;//used to keep track of how many times setInvSlotContents has been called after someone shift-clicks, so isTransferring can be false again
    private boolean isDistributing = false;//this is true when the inputs aren't equalized enough
    private boolean isAddingToStack = false;
	
	
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
		if (worldObj.isRemote && (itemstack != null && items[slot] != null && itemstack.getItem().equals(items[slot].getItem()) && itemstack.stackSize >= items[slot].stackSize) || itemstack == null)
			PacketHandler.sendInterfacePacket((byte)2, 0);
//		System.out.println("setInventorySlotContents");
		
//		if (worldObj.isRemote && itemstack != null && items[slot] != null && itemstack.getItem().equals(items[slot].getItem()) && itemstack.stackSize >= items[slot].stackSize)
//			distributeItems();
		
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
			int smallestInput = 0;
			int smallestIndex = findExtremumIndex(0);
			if (transferPass == 0) {//need this because it is reset each time
				smallestInput = items[smallestIndex] != null ? items[smallestIndex].stackSize : 66;
			}
			if (transferPass == smallestInput * 9) {//this method is called once for each item it decrements
				this.transferPass = 0;//reset
				this.isTransferring = false;//allow future actions on client side to call sendInterfacePacket
			}else{
				this.transferPass++;//the counter
			}
		}
	}

	@Override
	public String getInventoryName() {
		return "InventoryCompactor";
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
	public void openInventory() {}

	@Override
	public void closeInventory() {}

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
		NBTTagList list = compound.getTagList("Items", 0);
		items = new ItemStack[this.getSizeInventory()];
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound tagCompound = (NBTTagCompound)list.getCompoundTagAt(i);
			int slot = tagCompound.getByte("Slot");
			
			if (slot >= 0 && slot < getSizeInventory()) {
				items[slot] = ItemStack.loadItemStackFromNBT(tagCompound);
			}
		}
	}
	
	/**set the field smallestIn to the input found to have the smallest stack size, used for incrementing transferPass
	 *@param type 0 means find the smallest and check if its compactable, 1 means just find the smallest, 2 means find the max
	 */
	public int findExtremumIndex(int type) {
		int returnInt = 0;
		int returnIndex = 1;
		if (type == 0) {
			if (determineIfFilled() && determineIfHomogenous()) {
				returnInt = 66;//any stack should be smaller than this, its just a starting point
				for (int i = 0; i < items.length - 1; i++) {
					if (items[i].stackSize < returnInt) {
						returnIndex = i;
						returnInt = items[i].stackSize;
					}
				}
			}
		}else if (type == 1) {//the minimum
			returnInt = 66;//any stack should be smaller than this, its just a starting point
			for (int i = 0; i < items.length - 1; i++) {
				if (items[i] != null ? (items[i].stackSize < returnInt) : (0 < returnInt)) {//if there's an item there, see if its stack is smaller than the last smallest one, else 0
					returnIndex = i;
					returnInt = items[i] != null ? items[i].stackSize : 0;
				}
			}
		}else if(type == 2) {//the maximum
			returnInt = 0;//any stack should be bigger than this, its just a starting point
			for (int i = 0; i < items.length - 1; i++) {
				if (items[i] != null ? (items[i].stackSize > returnInt) : (false)) {
					returnIndex = i;
					returnInt = items[i].stackSize;
				}
			}
		}
		return returnIndex;
	}
	
	/**
	 * this determines whether the inputs are valid and whether an output should be set
	 */
	public void checkForCompacting(boolean shouldDecrement) {
		if (shouldDecrement) {//decrement upon removing the output
			decrementInputs();
			shouldDecrement = false;
		}
		
		if (!isDistributing)
			distributeItems();
		
		if (determineIfHomogenous() && determineIfFilled()) {
			isValidInput = true;//it was determined that the inputs are valid
		}
		if (isValidInput) {
			ItemStack itemStack = determineOutput(Item.getIdFromItem(items[4].getItem()),items[4].getItemDamage());//itemstack with stackSize of 1, id of squareTemplate, and damage of the inputs
			setItem(9, itemStack);//don't want it to call checkForCompacting, though we do want the updates to be sent
			isValidInput = false;//might no longer be valid
		}
		container.detectAndSendChanges();
	}
	
	/**
	 * Returns true if the item there is actually changed, returns false if nothing was changed
	 * @param index the index of the slot to set
	 * @param itemStack the itemstack to set the slot to
	 * @return whether or not it actually did anything
	 */
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
	
	/**
	 * Determines if everything in the compacting grid is the same item
	 * @return whether or not the inputs homogenous
	 */
	public boolean determineIfHomogenous() {
		for (int i = 0; i < items.length - 1; i++) {
			if (items[i] != null) {
				int itemID = Item.getIdFromItem(items[i].getItem());
				for (int k = i; k < items.length - 1; k++) {
					if (items[k] != null && Item.getIdFromItem(items[k].getItem()) != itemID) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Determines if the compacting grid is filled, will only be called if it's homogenous
	 * @return whether or not the inputs grid is filled
	 */
	public boolean determineIfFilled() {
		for (int i = 0; i < items.length - 1; i++) {
			if (items[i] == null) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Equally distributes the items in the crafting grid between each other 
	 */
	public void distributeItems() {
		int maxIndex = findExtremumIndex(2);
		int minIndex = findExtremumIndex(1);
		int minStackSize;
		
		if (items[minIndex] != null)
			minStackSize = items[minIndex] != null ? items[minIndex].stackSize : 0;
		else
			minStackSize = 0;
		
		if (Math.abs((items[maxIndex] != null ? items[maxIndex].stackSize : 0) - minStackSize) > 1) 
			isDistributing = true;
		else {
			if (isDistributing)
				checkForCompacting(false);
			
			isDistributing = false;
		}
		
		if (isDistributing) {
			decrStackSize(maxIndex, 1);
			if(items[minIndex] != null) {
				if (items[minIndex].stackSize <= items[minIndex].getMaxStackSize())
					items[minIndex].stackSize++;
			}else{
				items[minIndex] = new ItemStack(Item.getIdFromItem(items[maxIndex].getItem()), 1, items[maxIndex].getItemDamage());
			}
		}
		System.out.println(worldObj.isRemote);
	}
	
	public ItemStack determineOutput(int itemID, int itemMetadata) {
		int newItemDamage = 0;
		int newID = itemID << 8;
		newItemDamage = newID | itemMetadata;
		return new ItemStack(BlockInfo.SQTEMPLATE_ID, 1, newItemDamage);
	}
	
	/**
	 * well... it goes through each of the input stacks and decrements it
	 */
	private void decrementInputs() {
		this.isDecrementing = true;
		for (int i = 0; i < items.length - 1; i++) {
			decrStackSize(i, 1);
		}
		this.isDecrementing = false;
	}
	
	/**
	 * server side end of sendInterfaceEvent
	 * @param eventID 0 will set the output to null and call checkForCompacting, decrementInputs; 1 will avoid calling checkForCompacting;
	 * 				  2 will distribute the inputs
	 * @param itemID I don't think i really use this at this point...
	 */
	public void recieveInterfaceEvent(byte eventID, int itemID) {
		switch (eventID) {
			case 0:
				setInventorySlotContents(9, null);
				break;
			case 1:
				setItem(9, null);
				break;
			case 2:
				distributeItems();
				break;
		}
	}

	public ItemStack getItemInSlot(int slotNumber) {
		return  items[slotNumber];
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[]{0,1,2,3,4,5,6,7,8,9};
	}

	@Override
	public boolean canInsertItem(int slotIndex, ItemStack itemstack, int side) {
		return slotIndex >= 0 && slotIndex < 9;
	}

	@Override
	public boolean canExtractItem(int slotIndex, ItemStack itemstack, int side) {
		return slotIndex == 9;
	}
	
	@Override
	public void updateEntity() {
		if (isDistributing)
			distributeItems();
		super.updateEntity();
	}

	@Override
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return false;
	}
}
