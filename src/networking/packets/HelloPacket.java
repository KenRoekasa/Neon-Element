package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class HelloPacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    //
    // = 0 bytes

    protected HelloPacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
        super(PacketType.HELLO, ipAddress, port);
    }

    public HelloPacket() {
        super(PacketType.HELLO);
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        return Packet.getBytesFromBuffer(buffer);
    }

}
