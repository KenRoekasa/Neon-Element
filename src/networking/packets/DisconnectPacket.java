package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class DisconnectPacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    //
    // = 0 bytes

    protected DisconnectPacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
        super(PacketType.DISCONNECT, ipAddress, port);
    }

    public DisconnectPacket() {
        super(PacketType.DISCONNECT);
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        return Packet.getBytesFromBuffer(buffer);
    }

}
