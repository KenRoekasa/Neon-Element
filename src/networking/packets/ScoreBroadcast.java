package networking.packets;

import networking.client.ClientNetworkHandler;

import java.nio.ByteBuffer;

public class ScoreBroadcast extends Packet.PacketToClient {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // int + int
    // 4   + 4   = 8 bytes

    private int id;
    private int playerScore;

    protected ScoreBroadcast(ByteBuffer buffer, Sender sender) {
        super(sender);
        this.id = buffer.getInt();
        this.playerScore = buffer.getInt();
    }

    public ScoreBroadcast(int id, int playerScore) {
        super();
        this.playerScore = playerScore;
        this.id = id;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SCORE_BCAST;
    }

    public int getId() {
        return id;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    @Override
    public void handle(ClientNetworkHandler handler) {
        handler.recieveScoreBroadcast(this);
    }

    @Override
    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.putInt(this.id);
        buffer.putInt(this.playerScore);
        return Packet.getBytesFromBuffer(buffer);
    }

}
