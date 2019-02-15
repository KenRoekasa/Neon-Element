package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class DisconnectPacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    //
    // = 0 bytes

    protected DisconnectPacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
        super(PacketDirection.INCOMING, PacketType.DISCONNECT, ipAddress, port);
    }

    public DisconnectPacket(InetAddress ipAddress, int port) {
        super(PacketDirection.OUTGOING, PacketType.DISCONNECT, ipAddress, port);
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        return Packet.getBytesFromBuffer(buffer);
    }

}
