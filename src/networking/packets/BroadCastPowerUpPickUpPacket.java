package networking.packets;

import java.nio.ByteBuffer;

public class BroadCastPowerUpPickUpPacket extends Packet {

	 private int powerupPickupId;

	protected BroadCastPowerUpPickUpPacket(ByteBuffer buffer, Sender sender) {
		super(sender);
		this.powerupPickupId = buffer.getInt();
	}

	public BroadCastPowerUpPickUpPacket(int powerupPickupId) {
		super();
		this.powerupPickupId = powerupPickupId;
	}

    public PacketType getPacketType() {
       return PacketType.POWERUP;
    }

	public int getPowerupPickupId() {
		return this.powerupPickupId;
	}

	public byte[] getRawBytes() {
		ByteBuffer buffer = this.getByteBuffer();
		buffer.putInt(this.powerupPickupId);
		return Packet.getBytesFromBuffer(buffer);
	}

}
