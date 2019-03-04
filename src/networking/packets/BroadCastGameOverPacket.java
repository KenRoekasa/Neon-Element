package networking.packets;

import java.nio.ByteBuffer;

public class BroadCastGameOverPacket extends Packet {

	// Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // boolean
    // 1 = 1 bytes

    private boolean gameOver;

    protected BroadCastGameOverPacket(ByteBuffer buffer, Sender sender) {
        super(PacketType.GAME_OVER_BCAST, sender);
        this.gameOver = getBooleanValue(buffer.get());
    }

    public BroadCastGameOverPacket(boolean gameOver) {
        super(PacketType.GAME_OVER_BCAST);
        this.gameOver = gameOver;
    }

    public boolean getGameOverVar() {
        return this.gameOver;
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        	//this identifier has been placed twice
        buffer.put(getByteValue(this.getGameOverVar()));
        return Packet.getBytesFromBuffer(buffer);
    }

}
