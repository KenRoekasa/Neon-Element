package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class ConnectPacket extends Packet {
    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    //
    // = 0 bytes

    protected ConnectPacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
        super(PacketType.CONNECT, ipAddress, port);
    }

    public ConnectPacket() {
        super(PacketType.CONNECT);
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        return Packet.getBytesFromBuffer(buffer);
    }

}
