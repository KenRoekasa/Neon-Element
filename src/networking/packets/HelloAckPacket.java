package networking.packets;

import java.nio.ByteBuffer;

import engine.gameTypes.*;
import networking.client.ClientNetworkDispatcher;

public class HelloAckPacket extends Packet.PacketToClient {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // int + int + byte + long/int
    // 4   + 4   + 1    + 8        = 17 bytes

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
            case Timed:
                buffer.putLong(((TimedGame) gameType).getDuration());
        }
    }

    @Override
    public void handle(ClientNetworkDispatcher dispatcher) {
        dispatcher.receiveHelloAck(this);
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
