package Networking.Packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class ConnectPacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    //
    // = 0 bytes

    protected ConnectPacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
        super(PacketDirection.INCOMING, PacketType.CONNECT, ipAddress, port);
    }

    public ConnectPacket(InetAddress ipAddress, int port) {
        super(PacketDirection.OUTGOING, PacketType.CONNECT, ipAddress, port);
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        return Packet.getBytesFromBuffer(buffer);
    }

}
