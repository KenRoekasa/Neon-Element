package networking.packets;

import java.nio.ByteBuffer;

import networking.packets.Packet.PacketDirection;
import networking.packets.Packet.PacketType;

public class BroadCastDisconnectedUserPacket extends Packet {

	 // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    //
    // = 0 bytes

    protected BroadCastDisconnectedUserPacket(ByteBuffer buffer) {
        super(PacketDirection.INCOMING, PacketType.DISCONNECT_BCAST);
    }

    public BroadCastDisconnectedUserPacket() {
        super(PacketDirection.OUTGOING, PacketType.DISCONNECT_BCAST);
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        return Packet.getBytesFromBuffer(buffer);
    }


}
