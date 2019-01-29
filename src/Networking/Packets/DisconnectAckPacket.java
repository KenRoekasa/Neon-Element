package Networking.Packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class DisconnectAckPacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // boolean
    // 1 = 1 bytes

    private boolean allowed;

    protected DisconnectAckPacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
        super(PacketDirection.INCOMING, PacketType.DISCONNECT_ACK, ipAddress, port);
        this.allowed = getBooleanValue(buffer.get());
    }

    public DisconnectAckPacket(InetAddress ipAddress, int port, boolean allowed) {
        super(PacketDirection.OUTGOING, PacketType.DISCONNECT_ACK, ipAddress, port);
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
