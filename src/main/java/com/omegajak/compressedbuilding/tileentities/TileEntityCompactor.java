package com.omegajak.compressedbuilding.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

import com.omegajak.compressedbuilding.blocks.BlockCompactor;
import com.omegajak.compressedbuilding.items.ItemSquareTemplate;

public class TileEntityCompactor extends TileEntity implements IInventory {
	
	private ItemStack[] items;
	int num = 0;
	
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
		ItemStack itemstack = getStackInSlot(slot);
		
		if (itemstack != null) {
			if (itemstack.stackSize <= count) {
				setInventorySlotContents(slot, null);
			}else{
				itemstack = itemstack.splitStack(count);
				updateEntity();
			}
		}

		return itemstack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack item = getStackInSlot(slot);
		setInventorySlotContents(slot, null);
		return item;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) {
		items[slot] = itemstack;
		
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
		updateEntity();
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
		
		NBTTagList items = new NBTTagList();
		
		for (int i = 0; i < getSizeInventory(); i++) {
			ItemStack stack = getStackInSlot(i);
			
			if (stack != null) {
				NBTTagCompound item = new NBTTagCompound();
				item.setByte("Slot", (byte)i);
				stack.writeToNBT(item);
				items.appendTag(item);
			}
		}
		
		compound.setTag("Items", items);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		//not entirely sure what this does...
		NBTTagList items = compound.getTagList("Items", 0);
		
		for (int i = 0; i < items.tagCount(); i++) {
			NBTTagCompound item = (NBTTagCompound)items.getCompoundTagAt(i);
			int slot = item.getByte("Slot");
			
			if (slot >= 0 && slot < getSizeInventory()) {
				setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item));
			}
		}
	}
	
	public void checkForCompacting() {
		if (!worldObj.isRemote) {
			System.out.println("checkForCompacting was called for the " + num + " time on the " + sideToString() + " side");
			num++;
			distributeItems();
			if (determineIfHomogenous()) {
				if (determineIfFilled()) {
					System.out.println("Success!");
					setInventorySlotContents(9, determineOutput());
					decrementInputs();
				}
			}
		}
	}
	
	private String sideToString() {
		if (worldObj.isRemote) {
			return "client";
		}else{
			return "server";
		}
	}
	
	//Determines if everything in the compacting grid is the same item
	public boolean determineIfHomogenous() {
		for (int i = 0; i < items.length - 1; i++) {
			if (items[i] != null) {
				String itemName = items[i].getDisplayName();
				for (int k = i; k < items.length - 1; k++) {
					if (items[k] != null && items[k].getDisplayName() != itemName) {
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
	
	public ItemStack determineOutput() {
		return new ItemStack(new ItemSquareTemplate(new BlockCompactor()), 1, 0);
	}
	
	private void decrementInputs() {
		for (int i = 0; i < items.length - 1; i++) {
			items[i].stackSize--;
		}
	}

	@Override
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return false;
	}
}
