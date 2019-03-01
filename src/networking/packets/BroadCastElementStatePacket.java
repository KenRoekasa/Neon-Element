package networking.packets;

import java.nio.ByteBuffer;

import engine.enums.Elements;

public class BroadCastElementStatePacket extends Packet {

	// Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // byte
    // 1 = 1 bytes

    private Elements playerElementState;
    private int id;

    protected BroadCastElementStatePacket(ByteBuffer buffer) {
        super(PacketDirection.INCOMING, PacketType.ELEMENT_STATE_BCAST);
        this.id = buffer.getInt();
        this.playerElementState = Elements.getById(buffer.get());
    }

    public BroadCastElementStatePacket(int id, Elements playerElementState) {
        super(PacketDirection.OUTGOING, PacketType.ELEMENT_STATE_BCAST);
        this.id = id;
        this.playerElementState = playerElementState;
    }

    public Elements getPlayerElementState() {
        return this.playerElementState;
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.putInt(this.id);
        buffer.put(this.playerElementState.getId());

        return Packet.getBytesFromBuffer(buffer);
    }


}
