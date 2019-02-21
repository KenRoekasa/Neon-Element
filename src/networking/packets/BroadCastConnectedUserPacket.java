package networking.packets;

import java.nio.ByteBuffer;

public class BroadCastConnectedUserPacket extends Packet {

	private int playerId;

	protected BroadCastConnectedUserPacket(ByteBuffer buffer) {
		super(PacketDirection.INCOMING, PacketType.CONNECT_BCAST);
		this.playerId = buffer.getInt();

	}

	public BroadCastConnectedUserPacket(int playerId) {
		super(PacketDirection.OUTGOING, PacketType.CONNECT_BCAST);
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
