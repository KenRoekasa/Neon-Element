package networking.packets;

import java.nio.ByteBuffer;

public class ConnectPacket extends Packet {
    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    //
    // = 0 bytes

    protected ConnectPacket(ByteBuffer buffer, Sender sender) {
        super(sender);
    }

    public ConnectPacket() {
        super();
    }

    public PacketType getPacketType() {
       return PacketType.CONNECT;
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        return Packet.getBytesFromBuffer(buffer);
    }

}
