package networking.packets;

import java.nio.ByteBuffer;

import networking.server.ServerNetworkHandler;

public class PowerUpPacket extends Packet.PacketToServer {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // int
    // 4 = 4 bytes

    private int powerupPickupId;

    protected PowerUpPacket(ByteBuffer buffer, Sender sender) {
        super(sender);
        this.powerupPickupId = buffer.getInt();
    }

    public PowerUpPacket(int powerupPickupId) {
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
    public void handle(ServerNetworkHandler handler) {
        // TODO handle packet
    }

    @Override
    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.putInt(this.powerupPickupId);
        return Packet.getBytesFromBuffer(buffer);
    }

}
