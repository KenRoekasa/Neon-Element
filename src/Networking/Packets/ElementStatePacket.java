package Networking.Packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

import Enums.PlayerStates;

public class ElementStatePacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // byte
    // 1 = 1 bytes

    private PlayerStates playerState;

    protected ElementStatePacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
        super(PacketDirection.INCOMING, PacketType.ELEMENT_STATE, ipAddress, port);
        this.playerState = PlayerStates.getById(buffer.get());
    }

    public ElementStatePacket(InetAddress ipAddress, int port, PlayerStates playerState) {
        super(PacketDirection.OUTGOING, PacketType.ELEMENT_STATE, ipAddress, port);
        this.playerState = playerState;
    }

    public PlayerStates getPlayerState() {
        return this.playerState;
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.put(this.playerState.getId());
        return Packet.getBytesFromBuffer(buffer);
    }

}
