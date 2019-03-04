package networking.packets;

import java.nio.ByteBuffer;

import networking.server.ServerNetworkHandler;

public class LocationStatePacket extends Packet.PacketToServer {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // double + double
    // 8      + 8      = 16 bytes

    private double x;
    private double y;
	private double playerAngle;

    protected LocationStatePacket(ByteBuffer buffer, Sender sender) {
        super(sender);
        this.x = buffer.getDouble();
        this.y = buffer.getDouble();
        this.playerAngle = buffer.getDouble();
    }

    public LocationStatePacket(double x, double y, double playerAngle) {
        super();
        this.x = x;
        this.y = y;
        this.playerAngle = playerAngle;
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

    public double getPlayerAngle() {
        return this.playerAngle;
    }

    @Override
    public void handle(ServerNetworkHandler handler) {
        handler.receiveLocationState(this);
    }

    @Override
    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.putDouble(this.x);
        buffer.putDouble(this.y);
        buffer.putDouble(this.playerAngle);
        return Packet.getBytesFromBuffer(buffer);
    }

}
