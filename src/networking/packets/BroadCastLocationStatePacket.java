package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

import networking.packets.Packet.PacketDirection;
import networking.packets.Packet.PacketType;

public class BroadCastLocationStatePacket extends Packet {


    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // int + int
    // 4   + 4   = 8 bytes

    private int x;
    private int y;

    protected BroadCastLocationStatePacket(ByteBuffer buffer) {
        super(PacketDirection.INCOMING, PacketType.LOCATION_STATE_BCAST);
        this.x = buffer.getInt();
        this.y = buffer.getInt();
    }

    public BroadCastLocationStatePacket(int x, int y) {
        super(PacketDirection.OUTGOING, PacketType.LOCATION_STATE_BCAST);
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
