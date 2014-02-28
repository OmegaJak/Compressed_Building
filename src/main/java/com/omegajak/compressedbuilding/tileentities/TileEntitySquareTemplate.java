package com.omegajak.compressedbuilding.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntitySquareTemplate extends TileEntity{
	
	private int itemID;
	
	public TileEntitySquareTemplate(int itemID) {
		this.itemID = itemID;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		compound.setInteger("itemID", itemID);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		itemID = compound.getInteger("itemID");
	}
}
