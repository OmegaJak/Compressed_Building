package com.omegajak.compressedbuilding.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import com.omegajak.compressedbuilding.tileentities.TileEntityCompactor;

public class PacketCompactor extends AbstractPacket {
	
	private int eventID, x, y, z;
	
	public PacketCompactor() {}
	
	public PacketCompactor(byte eventID, int x, int y, int z) {
		this.eventID = eventID;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeByte(eventID);
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		eventID = buffer.readByte();
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {

	}

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
