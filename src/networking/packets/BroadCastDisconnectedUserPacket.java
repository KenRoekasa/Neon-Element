package networking.packets;

import java.nio.ByteBuffer;

public class BroadCastDisconnectedUserPacket extends Packet {

	 // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    //
    // = 0 bytes

    protected BroadCastDisconnectedUserPacket(ByteBuffer buffer, Sender sender) {
        super(PacketType.DISCONNECT_BCAST, sender);
    }

    public BroadCastDisconnectedUserPacket() {
        super(PacketType.DISCONNECT_BCAST);
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        return Packet.getBytesFromBuffer(buffer);
    }


}
