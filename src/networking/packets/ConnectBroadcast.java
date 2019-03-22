package networking.packets;

import java.nio.ByteBuffer;

import networking.client.ClientNetworkHandler;

public class ConnectBroadcast extends Packet.PacketToClient {

	private int playerId;

	protected ConnectBroadcast(ByteBuffer buffer, Sender sender) {
		super(sender);
		this.playerId = buffer.getInt();
	}

	public ConnectBroadcast(int playerId) {
		super();
		this.playerId = playerId;
	}

	@Override
    public PacketType getPacketType() {
       return PacketType.CONNECT_BCAST;
    }

	public int getId() {
	    return this.playerId;
	}

	@Override
	public void handle(ClientNetworkHandler handler) {
		// TODO handle packet
	}

	@Override
	public byte[] getRawBytes() {
		ByteBuffer buffer = this.getByteBuffer();
		buffer.putInt(this.playerId);
		return Packet.getBytesFromBuffer(buffer);
	}

}
