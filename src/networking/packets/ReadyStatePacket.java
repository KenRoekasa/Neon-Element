package networking.packets;

import java.nio.ByteBuffer;

import networking.server.ServerNetworkHandler;

public class ReadyStatePacket extends Packet.PacketToServer {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // boolean
    // 1 = 1 bytes

    private boolean ready;

    protected ReadyStatePacket(ByteBuffer buffer, Sender sender) {
        super(sender);
        this.ready = getBooleanValue(buffer.get());
    }

    public ReadyStatePacket(boolean ready) {
        super();
        this.ready = ready;
    }

    @Override
    public PacketType getPacketType() {
       return PacketType.READY_STATE;
    }

    public boolean getReady() {
        return this.ready;
    }

    @Override
    public void handle(ServerNetworkHandler handler) {
        // TODO handle packet
    }

    @Override
    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.put(getByteValue(this.ready));
        return Packet.getBytesFromBuffer(buffer);
    }

}
