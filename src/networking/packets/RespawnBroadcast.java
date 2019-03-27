package networking.packets;

import networking.client.ClientNetworkHandler;

import java.nio.ByteBuffer;

public class RespawnBroadcast extends Packet.PacketToClient {


    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // int + double + double
    // 4   + 8      + 8      = 20 bytes

    private int id;
    private double x;
    private double y;

    protected RespawnBroadcast(ByteBuffer buffer, Sender sender) {
        super(sender);
        this.id = buffer.getInt();
        this.x = buffer.getDouble();
        this.y = buffer.getDouble();
    }

    public RespawnBroadcast(int id, double x, double y) {
        super();
        this.id = id;
        this.x = x;
        this.y = y;
    }

    @Override
    public PacketType getPacketType() {
       return PacketType.RESPAWN_BCAST;
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

    @Override
    public void handle(ClientNetworkHandler handler) {
        handler.receiveRespawnBroadcast(this);
    }

    @Override
    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.putInt(this.id);
        buffer.putDouble(this.x);
        buffer.putDouble(this.y);
        return Packet.getBytesFromBuffer(buffer);
    }

}
