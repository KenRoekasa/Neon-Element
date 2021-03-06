package networking.packets;

import java.nio.ByteBuffer;

import networking.client.ClientNetworkHandler;

public class ReadyStateBroadcast extends Packet.PacketToClient {

	// Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // boolean
    // 1 = 1 bytes

    private boolean ready;

    protected ReadyStateBroadcast(ByteBuffer buffer, Sender sender) {
        super(sender);
        this.ready = getBooleanValue(buffer.get());
    }

    public ReadyStateBroadcast(boolean ready) {
        super();
        this.ready = ready;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.READY_STATE_BCAST;
    }

    public boolean getReady() {
        return this.ready;
    }

    @Override
    public void handle(ClientNetworkHandler handler) {
        // TODO handle packet
    }

    @Override
    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.put(getByteValue(this.ready));
        return Packet.getBytesFromBuffer(buffer);
    }
}
