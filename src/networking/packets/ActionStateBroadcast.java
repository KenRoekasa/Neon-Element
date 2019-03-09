package networking.packets;

import engine.enums.Action;
import networking.client.ClientNetworkHandler;

import java.nio.ByteBuffer;

public class ActionStateBroadcast extends Packet.PacketToClient {

    private Action playerActionState;

    private int id;

    protected ActionStateBroadcast(ByteBuffer buffer, Sender sender) throws Exception {
        super(sender);
        this.id = buffer.getInt();
        this.playerActionState = Action.getById(buffer.get());
    }

    public ActionStateBroadcast(int id, Action playerActionState) {
        super();
        this.id = id;
        this.playerActionState = playerActionState;
    }

    @Override
    public PacketType getPacketType() {
       return PacketType.ACTION_BCAST;
    }

    public Action getPlayerActionState() {
        return playerActionState;
    }

    public int getId() {
        return id;
    }

    @Override
    public void handle(ClientNetworkHandler handler) {
        handler.receivePlayerActionBroadCast(this);
    }

    @Override
    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.putInt(this.id);
        buffer.put(this.playerActionState.getId());

        return Packet.getBytesFromBuffer(buffer);
    }
}
