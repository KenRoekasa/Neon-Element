package networking.packets;

import java.nio.ByteBuffer;

import engine.enums.Action;

public class ActionStatePacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // Action
    // 1      = bytes

    private Action action;

    protected ActionStatePacket(ByteBuffer buffer, Sender sender) throws Exception {
        super(sender);
        this.action = Action.getById(buffer.get());
    }

    public ActionStatePacket(Action action) {
        super();
        this.action = action;
    }

    public PacketType getPacketType() {
       return PacketType.ACTION_STATE;
    }

    public Action getAction() {
        return this.action;
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.put(this.action.getId());
        return Packet.getBytesFromBuffer(buffer);
    }

}
