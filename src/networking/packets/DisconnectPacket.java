package networking.packets;

import java.nio.ByteBuffer;

import networking.server.ServerNetworkHandler;

public class DisconnectPacket extends Packet.PacketToServer {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    //
    // = 0 bytes

    protected DisconnectPacket(ByteBuffer buffer, Sender sender) {
        super(sender);
    }

    public DisconnectPacket() {
        super();
    }

    public PacketType getPacketType() {
       return PacketType.DISCONNECT;
    }

    @Override
    public void handle(ServerNetworkHandler handler) {
        // TODO handle packet
    }

    @Override
    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        return Packet.getBytesFromBuffer(buffer);
    }

}
