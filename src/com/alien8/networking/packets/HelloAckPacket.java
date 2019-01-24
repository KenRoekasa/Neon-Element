package com.alien8.networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class HelloAckPacket extends Packet {
    
    private int players;
    private int maxPlayers;

    protected HelloAckPacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
        super(PacketDirection.INCOMING, PacketType.HELLO_ACK, ipAddress, port);
        this.players = buffer.getInt();
        this.maxPlayers = buffer.getInt();
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
        byte[] data = new byte[Packet.PACKET_BYTES_LENGTH];
        ByteBuffer buffer = this.getByteBuffer();
        buffer.putInt(this.players);
        buffer.putInt(this.maxPlayers);
        buffer.get(data);
        return data;
    }

}
