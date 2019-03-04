package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class BroadCastConnectedUserPacket extends Packet {

	private int playerId;

	protected BroadCastConnectedUserPacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
		super(PacketType.CONNECT_BCAST, ipAddress, port);
		this.playerId = buffer.getInt();

	}

	public BroadCastConnectedUserPacket(int playerId) {
		super(PacketType.CONNECT_BCAST);
		this.playerId = playerId;
	}

	public int getId() {
	    return this.playerId;
	}

	@Override
	public byte[] getRawBytes() {
		ByteBuffer buffer = this.getByteBuffer();
		buffer.putInt(this.playerId);
		return Packet.getBytesFromBuffer(buffer);
	}

}
