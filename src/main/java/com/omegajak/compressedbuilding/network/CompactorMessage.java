package com.omegajak.compressedbuilding.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.tileentity.TileEntity;

import com.omegajak.compressedbuilding.tileentities.TileEntityCompactor;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

/*
 * See http://www.minecraftforge.net/forum/index.php/topic,20135.0.html
 * Many thanks
 */
public class CompactorMessage implements IMessage {
	
	private byte eventID; // Direction the entity at x,y,z is facing
	private int x;
	private int y;
	private int z;
	
	public CompactorMessage() { // Default
		this.eventID = -1;
		x = 0;
		y = 0;
		z = 0; 
	}
	
	public CompactorMessage(byte dir, int x, int y, int z) {
		this.eventID = dir;
		this.x  = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		eventID = buf.readByte();
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(eventID);
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}
	
	
	public static class Handler implements IMessageHandler<CompactorMessage, CompactorMessage> {
		@Override
		public CompactorMessage onMessage(CompactorMessage message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				TileEntityCompactor te = (TileEntityCompactor)ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z); // Gets the TileEntity on the Server's side
				switch (message.eventID) {
					case 0:
						te.setInventorySlotContents(9, null);
						break;
					case 1:
						te.setItem(9, null);
						break;
				}
				System.out.println("Received eventID of " + message.eventID + ", x of " + message.x + ", y of " + message.y + 
						", and z of " + message.z + "on the server side from " + ctx.getServerHandler().playerEntity.getDisplayName());
				return message; // Send the message back to the client who sent it
			} else {
				return null;
			}
		}
	}
}
