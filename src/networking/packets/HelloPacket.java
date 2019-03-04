package networking.packets;

import java.nio.ByteBuffer;

public class HelloPacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    //
    // = 0 bytes

    protected HelloPacket(ByteBuffer buffer, Sender sender) {
        super(sender);
    }

    public HelloPacket() {
        super();
    }

    public PacketType getPacketType() {
       return PacketType.HELLO;
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        return Packet.getBytesFromBuffer(buffer);
    }

}
