package networking.packets;

import java.nio.ByteBuffer;

import engine.enums.Elements;
import networking.server.ServerNetworkHandler;

public class ElementStatePacket extends Packet.PacketToServer {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // byte
    // 1 = 1 bytes

    private Elements playerElementState;

    protected ElementStatePacket(ByteBuffer buffer, Sender sender) throws Exception {
        super(sender);
        this.playerElementState = Elements.getById(buffer.get());
    }

    public ElementStatePacket(Elements playerElementState) {
        super();
        this.playerElementState = playerElementState;
    }

    @Override
    public PacketType getPacketType() {
       return PacketType.ELEMENT_STATE;
    }

    public Elements getPlayerElementState() {
        return this.playerElementState;
    }

    @Override
    public void handle(ServerNetworkHandler handler) {
        handler.receiveElementState(this);
    }

    @Override
    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.put(this.playerElementState.getId());
        return Packet.getBytesFromBuffer(buffer);
    }

}
