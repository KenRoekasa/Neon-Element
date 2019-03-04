package networking.packets;

import java.nio.ByteBuffer;

import networking.packets.Packet.PacketDirection;
import networking.packets.Packet.PacketType;

public class BroadCastLocationStatePacket extends Packet {


    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // int + double + double
    // 4   + 8      + 8      = 20 bytes

    private int id;
    private double x;
    private double y;
    private double playerAngle;

    protected BroadCastLocationStatePacket(ByteBuffer buffer) {
        super(PacketDirection.INCOMING, PacketType.LOCATION_STATE_BCAST);
        this.id = buffer.getInt();
        this.x = buffer.getDouble();
        this.y = buffer.getDouble();
        this.playerAngle = buffer.getDouble();
    }

    public BroadCastLocationStatePacket(int id, double x, double y, double playerAngle) {
        super(PacketDirection.OUTGOING, PacketType.LOCATION_STATE_BCAST);
        this.id = id;
        this.x = x;
        this.y = y;
        this.playerAngle = playerAngle;
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
    

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.putInt(this.id);
        buffer.putDouble(this.x);
        buffer.putDouble(this.y);
        buffer.putDouble(this.playerAngle);
        return Packet.getBytesFromBuffer(buffer);
    }

}
