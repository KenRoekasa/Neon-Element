package networking.packets;

import java.nio.ByteBuffer;

import networking.server.ServerNetworkHandler;

public class HelloPacket extends Packet.PacketToServer {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    //
    // = 0 bytes

    protected HelloPacket(ByteBuffer buffer, Sender sender) {
        super(sender);
    }

    public HelloPacket() {
        super();
    }

    public PacketType getPacketType() {
       return PacketType.HELLO;
    }

    @Override
    public void handle(ServerNetworkHandler handler) {
        handler.receiveHello(this);
    }

    @Override
    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        return Packet.getBytesFromBuffer(buffer);
    }

}
