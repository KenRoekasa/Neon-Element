package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class LocationStatePacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // double + double
    // 8      + 8      = 16 bytes

    private double x;
    private double y;
	private double playerAngle;

    protected LocationStatePacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
        super(PacketDirection.INCOMING, PacketType.LOCATION_STATE, ipAddress, port);
        this.x = buffer.getDouble();
        this.y = buffer.getDouble();
        this.playerAngle = buffer.getDouble();
    }

    public LocationStatePacket(double x, double y, double playerAngle, InetAddress ipAddress, int port) {
        super(PacketDirection.OUTGOING, PacketType.LOCATION_STATE, ipAddress, port);
        this.x = x;
        this.y = y;
        this.playerAngle = playerAngle;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }
    
    public double getPlayerAngle() {
        return this.playerAngle;
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.putDouble(this.x);
        buffer.putDouble(this.y);
        buffer.putDouble(this.playerAngle);
        return Packet.getBytesFromBuffer(buffer);
    }

}
