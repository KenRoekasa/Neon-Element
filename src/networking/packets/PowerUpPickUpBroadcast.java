package networking.packets;

import java.nio.ByteBuffer;

import networking.client.ClientNetworkHandler;

public class PowerUpPickUpBroadcast extends Packet.PacketToClient {

	 private int powerupPickupId;

	protected PowerUpPickUpBroadcast(ByteBuffer buffer, Sender sender) {
		super(sender);
		this.powerupPickupId = buffer.getInt();
	}

	public PowerUpPickUpBroadcast(int powerupPickupId) {
		super();
		this.powerupPickupId = powerupPickupId;
	}

	@Override
    public PacketType getPacketType() {
       return PacketType.POWERUP;
    }

	public int getPowerupPickupId() {
		return this.powerupPickupId;
	}

	@Override
	public void handle(ClientNetworkHandler handler) {
		// TODO handle packet
	}

	@Override
	public byte[] getRawBytes() {
		ByteBuffer buffer = this.getByteBuffer();
		buffer.putInt(this.powerupPickupId);
		return Packet.getBytesFromBuffer(buffer);
	}

}
