package com.omegajak.compressedbuilding.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

import com.omegajak.compressedbuilding.CompressedBuilding;
import com.omegajak.compressedbuilding.tileentities.TileEntityCompactor;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

public class PacketCompactor extends AbstractPacket {
	
	private int eventID, x, y, z;
	private byte direction =  3;
	
	public PacketCompactor() {}
	
	public PacketCompactor(byte eventID, int x, int y, int z) {
		this.eventID = eventID;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public PacketCompactor(byte eventID, int x, int y, int z, byte direction) {
		this.eventID = eventID;
		this.x = x;
		this.y = y;
		this.z = z;
		this.direction = direction;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeByte(eventID);
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeByte(direction);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		eventID = buffer.readByte();
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		direction = buffer.readByte();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		TileEntity te = player.worldObj.getTileEntity(x, y, z);
		if (te instanceof TileEntityCompactor) {
			switch(eventID) {
				case 0:
					((TileEntityCompactor)te).direction = this.direction;					
			}
		}
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
					CompressedBuilding.packetPipeline.sendTo(new PacketCompactor((byte)0, (int)x, (int)y, (int)z, ((TileEntityCompactor)te).direction), (EntityPlayerMP)player);
					break;
				case 4:
//					CompressedBuilding.packetPipeline.sendToAllAround(new PacketCompactor((byte)1, (int)x, (int)y, (int)z), new TargetPoint(player.dimension, x, y, z, 25));
//					((TileEntityCompactor)te).container.c;
					break;
			}
		}else{
			System.out.println("The tileEntity at " + x + ", " + y + ", " + z + ", " + "isn't a TileEntityCompactor");
		}
	}

}
