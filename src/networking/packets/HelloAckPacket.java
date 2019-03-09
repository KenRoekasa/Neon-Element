package networking.packets;

import java.nio.ByteBuffer;

import engine.gameTypes.*;
import networking.client.ClientNetworkHandler;

public class HelloAckPacket extends Packet.PacketToClient {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // Common to all game types:
    // int + int + byte
    // 4   + 4   + 1    = 9 bytes
    // Additional bytes for each type:
    // FirstToXKills: int
    // Timed:         long                           = 8             = 8
    // Hill:          double + double + double + int = 8 + 8 + 8 + 4 = 28
    // Regicide:      int + int                      = 4 + 4         = 8
    //
    // Maximum size = 9 + 28 = 37 bytes

    private int players;
    private int maxPlayers;
    private GameType gameType;

    protected HelloAckPacket(ByteBuffer buffer, Sender sender) throws Exception {
        super(sender);
        this.players = buffer.getInt();
        this.maxPlayers = buffer.getInt();
        this.gameType = bufferToGameType(buffer);
    }

    public HelloAckPacket(int players, int maxPlayers, GameType gameType) {
        super();
        this.players = players;
        this.maxPlayers = maxPlayers;
        this.gameType = gameType;
    }

    @Override
    public PacketType getPacketType() {
       return PacketType.HELLO_ACK;
    }

    public int getPlayers() {
        return this.players;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public GameType getGameType() {
        return this.gameType;
    }

    private static GameType bufferToGameType(ByteBuffer buffer) throws Exception {
        byte id = buffer.get();
        GameType.Type type = GameType.Type.getById(id);

        switch (type) {
            case FirstToXKills:
                return new FirstToXKillsGame(buffer.getInt());
            case Timed:
                return new TimedGame(buffer.getLong());
            case Hill:
                return new HillGame(buffer.getDouble(), buffer.getDouble(), buffer.getDouble(), buffer.getInt());
            case Regicide:
                return new Regicide(buffer.getInt(), buffer.getInt());
            default:
                return null;
        }
    }

    private static void gameTypeToBuffer(GameType gameType, ByteBuffer buffer) {
        GameType.Type type = gameType.getType();

        if (type != null) {
            buffer.put(type.getId());
        }
        switch (type) {
            case FirstToXKills:
                buffer.putInt(((FirstToXKillsGame) gameType).getKillsNeeded());
                break;
            case Timed:
                buffer.putLong(((TimedGame) gameType).getDuration());
                break;
            case Hill:
                buffer.putDouble(((HillGame) gameType).getHill().getCenterX());
                buffer.putDouble(((HillGame) gameType).getHill().getCenterY());
                buffer.putDouble(((HillGame) gameType).getHill().getRadius());
                buffer.putInt(((HillGame) gameType).getScoreNeeded());
                break;
            case Regicide:
                buffer.putInt(((Regicide) gameType).getKingId());
                buffer.putInt(((Regicide) gameType).getScoreNeeded());
                break;
        }
    }

    @Override
    public void handle(ClientNetworkHandler handler) {
        handler.receiveHelloAck(this);
    }

    @Override
    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.putInt(this.players);
        buffer.putInt(this.maxPlayers);
        gameTypeToBuffer(this.gameType, buffer);
        return Packet.getBytesFromBuffer(buffer);
    }

}
