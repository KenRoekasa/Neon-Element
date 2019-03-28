package networking.packets;

import networking.client.ClientNetworkHandler;

import java.nio.ByteBuffer;

public class ScoreBroadcast extends Packet.PacketToClient {


    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // int + int
    // 4   + 4   = 8 bytes
    private int kill;
    private int victimID;
    private int id;
    private int playerScore;


    protected ScoreBroadcast(ByteBuffer buffer, Sender sender) {
        super(sender);
        this.id = buffer.getInt();
        this.playerScore = buffer.getInt();
        this.kill = buffer.getInt();
        this.victimID = buffer.getInt();
    }

    public ScoreBroadcast(int id, int playerScore,int kill,int victimID) {
        super();
        this.playerScore = playerScore;
        this.kill = kill;
        this.victimID = victimID;
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


    public int getVictimID() {
        return victimID;
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
        buffer.putInt(this.kill);
        buffer.putInt(this.victimID);
        return Packet.getBytesFromBuffer(buffer);
    }

}
