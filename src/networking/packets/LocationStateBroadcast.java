package networking.packets;

import java.nio.ByteBuffer;

import networking.client.ClientNetworkHandler;

public class LocationStateBroadcast extends Packet.PacketToClient {


    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // int + double + double
    // 4   + 8      + 8      = 20 bytes

    private int id;
    private double x;
    private double y;
    private double playerAngle;

    protected LocationStateBroadcast(ByteBuffer buffer, Sender sender) {
        super(sender);
        this.id = buffer.getInt();
        this.x = buffer.getDouble();
        this.y = buffer.getDouble();
        this.playerAngle = buffer.getDouble();
    }

    public LocationStateBroadcast(int id, double x, double y, double playerAngle) {
        super();
        this.id = id;
        this.x = x;
        this.y = y;
        this.playerAngle = playerAngle;
    }

    public PacketType getPacketType() {
       return PacketType.LOCATION_STATE_BCAST;
    }

    public int getId() {
        return this.id;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getPlayerAngle() {
    		return playerAngle;
    }


    @Override
    public void handle(ClientNetworkHandler handler) {
        handler.receiveLocationStateBroadcast(this);
    }

    @Override
    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.putInt(this.id);
        buffer.putDouble(this.x);
        buffer.putDouble(this.y);
        buffer.putDouble(this.playerAngle);
        return Packet.getBytesFromBuffer(buffer);
    }

}
