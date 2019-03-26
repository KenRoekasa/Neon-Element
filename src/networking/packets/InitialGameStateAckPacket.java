package networking.packets;

import networking.server.ServerNetworkHandler;

import java.nio.ByteBuffer;

public class InitialGameStateAckPacket extends Packet.PacketToServer {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    //
    // = 0 bytes

    protected InitialGameStateAckPacket(ByteBuffer buffer, Sender sender) {
        super(sender);
    }

    public InitialGameStateAckPacket() {
        super();
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.INITIAL_STATE_ACK;
    }

    @Override
    public void handle(ServerNetworkHandler handler) {
        handler.receiveInitialGameStateAck(this);
    }

    @Override
    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        return Packet.getBytesFromBuffer(buffer);
    }

}
