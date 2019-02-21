package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

import engine.gameTypes.GameType;

public class HelloAckPacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // int + int + byte
    // 4   + 4   + 1    = 9 bytes

    private int players;
    private int maxPlayers;
    private GameType.Type gameType;

    protected HelloAckPacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
        super(PacketDirection.INCOMING, PacketType.HELLO_ACK, ipAddress, port);
        this.players = buffer.getInt();
        this.maxPlayers = buffer.getInt();
        this.gameType = GameType.Type.getById(buffer.get());
    }

    public HelloAckPacket(int players, int maxPlayers, GameType.Type gameType, InetAddress ipAddress, int port) {
        super(PacketDirection.OUTGOING, PacketType.HELLO_ACK, ipAddress, port);
        this.players = players;
        this.maxPlayers = maxPlayers;
        this.gameType = gameType;
    }

    public int getPlayers() {
        return this.players;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public GameType.Type getGameType() {
        return this.gameType;
    }

    @Override
    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.putInt(this.players);
        buffer.putInt(this.maxPlayers);
        buffer.put(this.gameType.getId());
        return Packet.getBytesFromBuffer(buffer);
    }

}
