package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class PowerUpPacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // int
    // 4 = 4 bytes

    private int powerupPickupId;

    protected PowerUpPacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
        super(PacketType.POWERUP, ipAddress, port);
        this.powerupPickupId = buffer.getInt();
    }

    public PowerUpPacket(int powerupPickupId) {
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
