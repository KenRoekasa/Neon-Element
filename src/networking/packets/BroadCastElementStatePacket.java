package networking.packets;

import java.nio.ByteBuffer;

import engine.enums.Elements;
import networking.client.ClientNetworkDispatcher;

public class BroadCastElementStatePacket extends Packet.PacketToClient {

	// Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // byte
    // 1 = 1 bytes

    private Elements playerElementState;
    private int id;

    protected BroadCastElementStatePacket(ByteBuffer buffer, Sender sender) throws Exception {
        super(sender);
        this.id = buffer.getInt();
        this.playerElementState = Elements.getById(buffer.get());
    }

    public BroadCastElementStatePacket(int id, Elements playerElementState) {
        super();
        this.id = id;
        this.playerElementState = playerElementState;
    }

    public PacketType getPacketType() {
       return PacketType.ELEMENT_STATE_BCAST;
    }

    public Elements getPlayerElementState() {
        return this.playerElementState;
    }

    public int getId() {
    		return id;
    }

    @Override
    public void handle(ClientNetworkDispatcher dispatcher) {
        dispatcher.receiveElementBroadcast(this);
    }

    @Override
    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.putInt(this.id);
        buffer.put(this.playerElementState.getId());

        return Packet.getBytesFromBuffer(buffer);
    }


}
