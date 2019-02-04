package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class ConnectAckPacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // boolean
    // 1 = 1 bytes

    private boolean allowed;

    protected ConnectAckPacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
        super(PacketDirection.INCOMING, PacketType.CONNECT_ACK, ipAddress, port);
        this.allowed = getBooleanValue(buffer.get());
    }

    public ConnectAckPacket(boolean allowed, InetAddress ipAddress, int port) {
        super(PacketDirection.OUTGOING, PacketType.CONNECT_ACK, ipAddress, port);
        this.allowed = allowed;
    }

    public boolean getAllowed() {
        return this.allowed;
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        	//this identifier has been placed twice
        buffer.put(getByteValue(this.allowed));
        return Packet.getBytesFromBuffer(buffer);
    }

}
