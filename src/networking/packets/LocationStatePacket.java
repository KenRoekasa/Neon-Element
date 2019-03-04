package networking.packets;

import java.nio.ByteBuffer;

public class LocationStatePacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // double + double
    // 8      + 8      = 16 bytes

    private double x;
    private double y;

    protected LocationStatePacket(ByteBuffer buffer, Sender sender) {
        super(sender);
        this.x = buffer.getDouble();
        this.y = buffer.getDouble();
    }

    public LocationStatePacket(double x, double y) {
        super();
        this.x = x;
        this.y = y;
    }

    public PacketType getPacketType() {
       return PacketType.LOCATION_STATE;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.putDouble(this.x);
        buffer.putDouble(this.y);
        return Packet.getBytesFromBuffer(buffer);
    }

}
