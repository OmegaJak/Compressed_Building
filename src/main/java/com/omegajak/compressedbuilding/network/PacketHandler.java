/**package com.omegajak.compressedbuilding.network;

import ibxm.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.omegajak.compressedbuilding.inventory.ContainerCompactor;
import com.omegajak.compressedbuilding.lib.ModInformation;
import com.omegajak.compressedbuilding.tileentities.TileEntityCompactor;

public class PacketHandler implements IPacketHandler {


	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		ByteArrayDataInput reader = ByteStreams.newDataInput(packet.data);
		
		EntityPlayer entityPlayer = (EntityPlayer)player;
		
		byte packetId = reader.readByte();
		
		switch (packetId) {
			case 0:
				byte eventID = reader.readByte();
				int itemID = reader.readInt();
				Container container = entityPlayer.openContainer;
				if (container != null && container instanceof ContainerCompactor) {
					TileEntityCompactor compactor = ((ContainerCompactor)container).getCompactor();
					compactor.recieveInterfaceEvent((byte)eventID, itemID);
				}
		}
		

	}
	

	public static void sendInterfacePacket(byte eventID, int itemID) {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(byteStream);

		try {
			dataStream.writeByte((byte)0);
			dataStream.writeByte(eventID);
			dataStream.writeInt(itemID);
			
			PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(ModInformation.CHANNEL, byteStream.toByteArray()));
		}catch(IOException ex) {
			System.err.append("Failed to send interface packet");
		}
	}
}*/
