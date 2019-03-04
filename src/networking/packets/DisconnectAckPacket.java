package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class DisconnectAckPacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // boolean
    // 1 = 1 bytes

    private boolean allowed;

    protected DisconnectAckPacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
        super(PacketType.DISCONNECT_ACK, ipAddress, port);
        this.allowed = getBooleanValue(buffer.get());
    }

    public DisconnectAckPacket(boolean allowed) {
        super(PacketType.DISCONNECT_ACK);
        this.allowed = allowed;
    }

    public boolean getAllowed() {
        return this.allowed;
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.put(getByteValue(this.allowed));
        return Packet.getBytesFromBuffer(buffer);
    }

}
