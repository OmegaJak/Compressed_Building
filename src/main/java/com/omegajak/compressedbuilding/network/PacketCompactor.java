package com.omegajak.compressedbuilding.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;

import com.omegajak.compressedbuilding.tileentities.TileEntityCompactor;

public class PacketCompactor extends AbstractPacket {
	
	private int eventID, x, y, z, itemID, itemDamage;
	
	public PacketCompactor() {}
	
	public PacketCompactor(byte eventID, int x, int y, int z) {
		this.eventID = eventID;
		this.x = x;
		this.y = y;
		this.z = z;
		itemID = -1;
		itemDamage = -1;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeByte(eventID);
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeInt(itemID);
		buffer.writeInt(itemDamage);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		eventID = buffer.readByte();
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		itemID = buffer.readInt();
		itemDamage = buffer.readInt();
	}

	/**
	 * A packet is received on the client side that was sent from the server side
	 */
	@Override
	public void handleClientSide(EntityPlayer player) {
		TileEntity te = player.worldObj.getTileEntity(x, y, z);
		if (te instanceof TileEntityCompactor) {
			switch (eventID) {
				case 0:
					((TileEntityCompactor)te).setItem(9, ((TileEntityCompactor)te).determineOutput(itemID, itemDamage));
			}
		}
	}

	/**
	 * A packet is received on the server side that was sent from the client side
	 */
	@Override
	public void handleServerSide(EntityPlayer player) {
		TileEntity te = player.worldObj.getTileEntity(x, y, z);
		if (te instanceof TileEntityCompactor) {
			switch (eventID) {
				case 0:
					((TileEntityCompactor)te).setInventorySlotContents(9, null);
					break;
				case 1:
					((TileEntityCompactor)te).setItem(9, null);
					break;
				case 2:
					((TileEntityCompactor)te).distributeItems();
					break;
				case 3:
					((TileEntityCompactor)te).checkForCompacting(false);
					break;
			}
		}
	}

}
