package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

import networking.packets.Packet.PacketDirection;
import networking.packets.Packet.PacketType;

public class BroadCastConnectedUserPacket extends Packet {

	String playerName;

	protected BroadCastConnectedUserPacket(ByteBuffer buffer) {
		super(PacketDirection.INCOMING, PacketType.CONNECT_BCAST);
		this.playerName = buffer.toString();

	}

	protected BroadCastConnectedUserPacket(boolean allowed) {
		super(PacketDirection.OUTGOING, PacketType.CONNECT_BCAST);

		// TODO Auto-generated constructor stub
	}

	@Override
	public byte[] getRawBytes() {
		// TODO Auto-generated method stub

		ByteBuffer buffer = this.getByteBuffer();
		buffer.put(getPlayerName().getBytes());
		return Packet.getBytesFromBuffer(buffer);
	}

	public String getPlayerName() {
		return this.playerName;
	}

}
