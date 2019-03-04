package networking.packets;

import java.nio.ByteBuffer;

import networking.server.ServerNetworkHandler;

public class ConnectPacket extends Packet.PacketToServer {
    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    //
    // = 0 bytes

    protected ConnectPacket(ByteBuffer buffer, Sender sender) {
        super(sender);
    }

    public ConnectPacket() {
        super();
    }

    public PacketType getPacketType() {
       return PacketType.CONNECT;
    }

    @Override
    public void handle(ServerNetworkHandler handler) {
        handler.receiveConnect(this);
    }

    @Override
    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        return Packet.getBytesFromBuffer(buffer);
    }

}
