package networking.packets;

import java.nio.ByteBuffer;

import networking.client.ClientNetworkDispatcher;

public class BroadCastConnectedUserPacket extends Packet.PacketToClient {

	private int playerId;

	protected BroadCastConnectedUserPacket(ByteBuffer buffer, Sender sender) {
		super(sender);
		this.playerId = buffer.getInt();

	}

	public BroadCastConnectedUserPacket(int playerId) {
		super();
		this.playerId = playerId;
	}

    public PacketType getPacketType() {
       return PacketType.CONNECT_BCAST;
    }

	public int getId() {
	    return this.playerId;
	}

	@Override
	public void handle(ClientNetworkDispatcher dispatcher) {
		// TODO handle packet
	}

	@Override
	public byte[] getRawBytes() {
		ByteBuffer buffer = this.getByteBuffer();
		buffer.putInt(this.playerId);
		return Packet.getBytesFromBuffer(buffer);
	}

}
