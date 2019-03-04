package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class BroadCastPowerUpPickUpPacket extends Packet {

	 private int powerupPickupId;

	protected BroadCastPowerUpPickUpPacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
		super(PacketType.POWERUP, ipAddress, port);
		this.powerupPickupId = buffer.getInt();
	}

	public BroadCastPowerUpPickUpPacket(int powerupPickupId) {
		super(PacketType.POWERUP);
		this.powerupPickupId = powerupPickupId;
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
