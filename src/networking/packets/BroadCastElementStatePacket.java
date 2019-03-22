package networking.packets;

import java.nio.ByteBuffer;

import engine.model.enums.Elements;

public class BroadCastElementStatePacket extends Packet {

	// Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // byte
    // 1 = 1 bytes

    private Elements playerElementState;

    protected BroadCastElementStatePacket(ByteBuffer buffer) {
        super(PacketDirection.INCOMING, PacketType.ELEMENT_STATE_BCAST);
        this.playerElementState = Elements.getById(buffer.get());
    }

    public BroadCastElementStatePacket(Elements playerElementState) {
        super(PacketDirection.OUTGOING, PacketType.ELEMENT_STATE_BCAST);
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
