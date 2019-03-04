package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class BroadCastDisconnectedUserPacket extends Packet {

	 // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    //
    // = 0 bytes

    protected BroadCastDisconnectedUserPacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
        super(PacketType.DISCONNECT_BCAST, ipAddress, port);
    }

    public BroadCastDisconnectedUserPacket() {
        super(PacketType.DISCONNECT_BCAST);
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        return Packet.getBytesFromBuffer(buffer);
    }


}
