package networking.packets;

import java.nio.ByteBuffer;

import networking.client.ClientNetworkHandler;

public class DisconnectBroadcast extends Packet.PacketToClient {

	 // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    //
    // = 0 bytes

    protected DisconnectBroadcast(ByteBuffer buffer, Sender sender) {
        super(sender);
    }

    public DisconnectBroadcast() {
        super();
    }

    @Override
    public PacketType getPacketType() {
       return PacketType.DISCONNECT_BCAST;
    }

    @Override
    public void handle(ClientNetworkHandler handler) {
        // TODO handle packet
    }

    @Override
    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        return Packet.getBytesFromBuffer(buffer);
    }

}
