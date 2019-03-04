package networking.packets;

import java.nio.ByteBuffer;

import engine.enums.Elements;

public class ElementStatePacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // byte
    // 1 = 1 bytes

    private Elements playerElementState;

    protected ElementStatePacket(ByteBuffer buffer, Sender sender) {
        super(sender);
        this.playerElementState = Elements.getById(buffer.get());
    }

    public ElementStatePacket(Elements playerElementState) {
        super();
        this.playerElementState = playerElementState;
    }

    public PacketType getPacketType() {
       return PacketType.ELEMENT_STATE;
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
