package com.alien8.networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class HelloAckPacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // int + int
    // 4   + 4   = 8 bytes

    private int players;
    private int maxPlayers;

    protected HelloAckPacket(String[] data, InetAddress ipAddress, int port) {
        super(PacketDirection.INCOMING, PacketType.HELLO_ACK, ipAddress, port);
        this.players = Integer.parseInt(data[1]);
        this.maxPlayers = Integer.parseInt(data[2]);
    }

    public HelloAckPacket(int players, int maxPlayers, InetAddress ipAddress, int port) {
        super(PacketDirection.OUTGOING, PacketType.HELLO_ACK, ipAddress, port);
        this.players = players;
        this.maxPlayers = maxPlayers;
    }

    public int getPlayers() {
        return this.players;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    @Override
    public byte[] getRawBytes() {
        String str = ";" + this.players + ";" + this.maxPlayers;
        byte[] strBytes = str.getBytes();
        byte[] data = new byte[strBytes.length + 1];
        data[0] = this.getType().getId();
        System.arraycopy(strBytes, 0, data, 1, strBytes.length);
        return data;
    }

}
