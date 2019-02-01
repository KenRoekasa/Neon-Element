package Networking.Packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

import enums.Elements;

public class ElementStatePacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // byte
    // 1 = 1 bytes

    private Elements playerElementState;

    protected ElementStatePacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
        super(PacketDirection.INCOMING, PacketType.ELEMENT_STATE, ipAddress, port);
        this.playerElementState = Elements.getById(buffer.get());
    }

    public ElementStatePacket(InetAddress ipAddress, int port, Elements playerElementState) {
        super(PacketDirection.OUTGOING, PacketType.ELEMENT_STATE, ipAddress, port);
        this.playerElementState = playerElementState;
    }

    public Elements getPlayerElementState() {
        return this.playerElementState;
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.put(this.playerElementState.getId());
        return Packet.getBytesFromBuffer(buffer);
    }

}
