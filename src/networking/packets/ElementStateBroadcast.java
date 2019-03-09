package networking.packets;

import java.nio.ByteBuffer;

import engine.enums.Elements;
import networking.client.ClientNetworkHandler;

public class ElementStateBroadcast extends Packet.PacketToClient {

	// Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // byte
    // 1 = 1 bytes

    private Elements playerElementState;
    private int id;

    protected ElementStateBroadcast(ByteBuffer buffer, Sender sender) throws Exception {
        super(sender);
        this.id = buffer.getInt();
        this.playerElementState = Elements.getById(buffer.get());
    }

    public ElementStateBroadcast(int id, Elements playerElementState) {
        super();
        this.id = id;
        this.playerElementState = playerElementState;
    }

    @Override
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
    public void handle(ClientNetworkHandler handler) {
        handler.receiveElementBroadcast(this);
    }

    @Override
    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.putInt(this.id);
        buffer.put(this.playerElementState.getId());

        return Packet.getBytesFromBuffer(buffer);
    }


}
