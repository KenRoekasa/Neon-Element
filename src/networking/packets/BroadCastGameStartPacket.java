package networking.packets;

import java.nio.ByteBuffer;

public class BroadCastGameStartPacket extends Packet {


	// Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // boolean + int
    // 1       + 4   = 1 bytes

    private boolean gameStarted;
    private int players;

    protected BroadCastGameStartPacket(ByteBuffer buffer, Sender sender) {
        super(sender);
        this.gameStarted = getBooleanValue(buffer.get());
        this.players = buffer.getInt();
    }

    public BroadCastGameStartPacket(boolean gameStarted, int players) {
        super();
        this.gameStarted = gameStarted;
        this.players = players;
    }

    public PacketType getPacketType() {
       return PacketType.GAME_START_BCAST;
    }

    public boolean getGameStartVar() {
        return this.gameStarted;
    }

    public int getPlayersCount() {
        return this.players;
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        	//this identifier has been placed twice
        buffer.put(getByteValue(this.getGameStartVar()));
        buffer.putInt(this.players);
        return Packet.getBytesFromBuffer(buffer);
    }
}
