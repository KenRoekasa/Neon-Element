package networking.packets;

import java.nio.ByteBuffer;

import networking.client.ClientNetworkDispatcher;

public class DisconnectAckPacket extends Packet.PacketToClient {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // boolean
    // 1 = 1 bytes

    private boolean allowed;

    protected DisconnectAckPacket(ByteBuffer buffer, Sender sender) {
        super(sender);
        this.allowed = getBooleanValue(buffer.get());
    }

    public DisconnectAckPacket(boolean allowed) {
        super();
        this.allowed = allowed;
    }

    public PacketType getPacketType() {
       return PacketType.DISCONNECT_ACK;
    }

    public boolean getAllowed() {
        return this.allowed;
    }

    @Override
    public void handle(ClientNetworkDispatcher dispatcher) {
        // TODO handle packet
    }

    @Override
    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.put(getByteValue(this.allowed));
        return Packet.getBytesFromBuffer(buffer);
    }

}
