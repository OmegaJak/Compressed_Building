package com.omegajak.compressedbuilding.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

import com.omegajak.compressedbuilding.blocks.BlockSquareTemplate;
import com.omegajak.compressedbuilding.blocks.Blocks;
import com.omegajak.compressedbuilding.lib.BlockInfo;
import com.omegajak.compressedbuilding.lib.ModInformation;

import cpw.mods.fml.common.registry.GameRegistry;

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
		ItemStack itemStack = getStackInSlot(slot);
		
		if (itemStack != null) {
			if (itemStack.stackSize <= count) {
				setInventorySlotContents(slot, null);
			}else{
				itemStack = itemStack.splitStack(count);
				if (itemStack.stackSize == 0)
                {
                    setInventorySlotContents(slot, null);
                }
			}
		}

		return itemStack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack itemStack = getStackInSlot(slot);
		if (itemStack != null) {
			setInventorySlotContents(slot, null);
		}
		return itemStack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) {
		items[slot] = itemstack;
		
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
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
	
	public void checkForCompacting() {
	//	if (!worldObj.isRemote) {
			System.out.println("checkForCompacting was called for the " + num + " time on the " + sideToString() + " side");
			num++;
			distributeItems();
			if (determineIfHomogenous()) {
				if (determineIfFilled()) {
					System.out.println("Success!");
					ItemStack itemStack = determineOutput();
					System.out.println(itemStack.getUnlocalizedName());
					decrementInputs();
					setInventorySlotContents(9, itemStack);
					markDirty();
				}
			}
//		}
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
				int lastID = Item.getIdFromItem(items[i].getItem());
				for (int k = i; k < items.length - 1; k++) {
					if (items[k] != null && Item.getIdFromItem(items[k].getItem())!= lastID) {
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
		return new ItemStack(Blocks.squareTemplate, 1, 0);
	}
	
	private void decrementInputs() {
		for (int i = 0; i < items.length - 1; i++) {
	//		items[i].stackSize--;
			decrStackSize(i, 1);
		}
	}

	@Override
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return false;
	}
}
