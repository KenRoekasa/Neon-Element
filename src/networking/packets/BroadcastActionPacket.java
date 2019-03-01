package networking.packets;

import engine.enums.Action;
import engine.enums.Elements;

import java.nio.ByteBuffer;

public class BroadcastActionPacket extends Packet {


    private Action playerActionState;

    private int id;

    protected BroadcastActionPacket(ByteBuffer buffer) {
        super(PacketDirection.INCOMING, PacketType.ACTION_BCAST);
        this.id = buffer.getInt();
        this.playerActionState = Action.getById(buffer.get());
    }

    public BroadcastActionPacket(int id, Action playerActionState) {
        super(PacketDirection.OUTGOING, PacketType.ELEMENT_STATE_BCAST);
        this.id = id;
        this.playerActionState = playerActionState;
    }


    public Action getPlayerActionState() {
        return playerActionState;
    }

    public int getId() {
        return id;
    }


    @Override
    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.putInt(this.id);
        buffer.put(this.playerActionState.getId());

        return Packet.getBytesFromBuffer(buffer);
    }
}
