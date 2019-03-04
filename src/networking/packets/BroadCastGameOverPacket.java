package networking.packets;

import java.nio.ByteBuffer;

import networking.client.ClientNetworkHandler;

public class BroadCastGameOverPacket extends Packet.PacketToClient {

	// Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // boolean
    // 1 = 1 bytes

    private boolean gameOver;

    protected BroadCastGameOverPacket(ByteBuffer buffer, Sender sender) {
        super(sender);
        this.gameOver = getBooleanValue(buffer.get());
    }

    public BroadCastGameOverPacket(boolean gameOver) {
        super();
        this.gameOver = gameOver;
    }

    public PacketType getPacketType() {
       return PacketType.GAME_OVER_BCAST;
    }

    public boolean getGameOverVar() {
        return this.gameOver;
    }

    @Override
    public void handle(ClientNetworkHandler handler) {
        // TODO handle packet
    }

    @Override
    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        	//this identifier has been placed twice
        buffer.put(getByteValue(this.getGameOverVar()));
        return Packet.getBytesFromBuffer(buffer);
    }

}
