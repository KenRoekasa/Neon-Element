package networking.packets;

import java.nio.ByteBuffer;

import networking.client.ClientNetworkHandler;

public class BroadCastDisconnectedUserPacket extends Packet.PacketToClient {

	 // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    //
    // = 0 bytes

    protected BroadCastDisconnectedUserPacket(ByteBuffer buffer, Sender sender) {
        super(sender);
    }

    public BroadCastDisconnectedUserPacket() {
        super();
    }

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
