package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

import networking.packets.Packet.PacketDirection;
import networking.packets.Packet.PacketType;

public class BroadCastGameStartPacket extends Packet {


	// Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // boolean
    // 1 = 1 bytes

    private boolean gameStarted;

    protected BroadCastGameStartPacket(ByteBuffer buffer) {
        super(PacketDirection.INCOMING, PacketType.GAME_START_BCAST);
        this.gameStarted = getBooleanValue(buffer.get());
    }

    public BroadCastGameStartPacket(boolean gameStarted) {
        super(PacketDirection.OUTGOING, PacketType.GAME_START_BCAST);
        this.gameStarted = gameStarted;
    }

    public boolean getGameStartVar() {
        return this.gameStarted;
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        	//this identifier has been placed twice
        buffer.put(getByteValue(this.getGameStartVar()));
        return Packet.getBytesFromBuffer(buffer);
    }
}