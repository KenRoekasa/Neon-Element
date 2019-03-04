package networking.packets;

import engine.enums.Action;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class BroadcastActionPacket extends Packet {

    private Action playerActionState;

    private int id;

    protected BroadcastActionPacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
        super(PacketType.ACTION_BCAST, ipAddress, port);
        this.id = buffer.getInt();
        this.playerActionState = Action.getById(buffer.get());
    }

    public BroadcastActionPacket(int id, Action playerActionState) {
        super(PacketType.ACTION_BCAST);
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
