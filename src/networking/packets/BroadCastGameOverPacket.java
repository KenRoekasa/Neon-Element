package networking.packets;

import java.nio.ByteBuffer;

import networking.packets.Packet.PacketDirection;
import networking.packets.Packet.PacketType;

public class BroadCastGameOverPacket extends Packet {

	// Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // boolean
    // 1 = 1 bytes

    private boolean gameOver;

    protected BroadCastGameOverPacket(ByteBuffer buffer) {
        super(PacketDirection.INCOMING, PacketType.GAME_OVER_BCAST);
        this.gameOver = getBooleanValue(buffer.get());
    }

    public BroadCastGameOverPacket(boolean gameOver) {
        super(PacketDirection.OUTGOING, PacketType.GAME_OVER_BCAST);
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