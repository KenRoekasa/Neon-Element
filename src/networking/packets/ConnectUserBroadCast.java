package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

import networking.packets.Packet.PacketDirection;
import networking.packets.Packet.PacketType;

public class ConnectUserBroadCast extends ConnectAckPacket {

	protected ConnectUserBroadCast(ByteBuffer buffer, InetAddress ipAddress, int port) {
		super(buffer, ipAddress, port);
		// TODO Auto-generated constructor stub
	}
	
	
}
