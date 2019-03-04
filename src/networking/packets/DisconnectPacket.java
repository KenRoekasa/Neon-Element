package networking.packets;

import java.nio.ByteBuffer;

public class DisconnectPacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    //
    // = 0 bytes

    protected DisconnectPacket(ByteBuffer buffer, Sender sender) {
        super(sender);
    }

    public DisconnectPacket() {
        super();
    }

    public PacketType getPacketType() {
       return PacketType.DISCONNECT;
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        return Packet.getBytesFromBuffer(buffer);
    }

}
