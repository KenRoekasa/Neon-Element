package networking.packets;

import java.nio.ByteBuffer;

public class PowerUpPacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // int
    // 4 = 4 bytes

    private int powerupPickupId;

    protected PowerUpPacket(ByteBuffer buffer, Sender sender) {
        super(PacketType.POWERUP, sender);
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
