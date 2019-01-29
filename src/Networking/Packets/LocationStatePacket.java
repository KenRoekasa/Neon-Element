package Networking.Packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class LocationStatePacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // int + int
    // 4   + 4   = 8 bytes

    private int x;
    private int y;

    protected LocationStatePacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
        super(PacketDirection.INCOMING, PacketType.LOCATION_STATE, ipAddress, port);
        this.x = buffer.getInt();
        this.y = buffer.getInt();
    }

    public LocationStatePacket(InetAddress ipAddress, int port, int x, int y) {
        super(PacketDirection.OUTGOING, PacketType.LOCATION_STATE, ipAddress, port);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.putInt(this.x);
        buffer.putInt(this.y);
        return Packet.getBytesFromBuffer(buffer);
    }

}
