package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

import networking.packets.Packet.PacketDirection;
import networking.packets.Packet.PacketType;

public class BroadCastPowerUpPickUpPacket extends Packet {

	 private int powerupPickupId;

	    protected BroadCastPowerUpPickUpPacket(ByteBuffer buffer) {
	        super(PacketDirection.INCOMING, PacketType.POWERUP);
	        this.powerupPickupId = buffer.getInt();
	    }

	    public BroadCastPowerUpPickUpPacket(int powerupPickupId) {
	        super(PacketDirection.OUTGOING, PacketType.POWERUP);
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
