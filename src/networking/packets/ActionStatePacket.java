package networking.packets;

import java.nio.ByteBuffer;

import engine.model.enums.Action;
import networking.server.ServerNetworkHandler;

public class ActionStatePacket extends Packet.PacketToServer {

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

    @Override
    public PacketType getPacketType() {
       return PacketType.ACTION_STATE;
    }

    public Action getAction() {
        return this.action;
    }

    @Override
    public void handle(ServerNetworkHandler handler) {
        handler.receiveActionState(this);
    }

    @Override
    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.put(this.action.getId());
        return Packet.getBytesFromBuffer(buffer);
    }

}
