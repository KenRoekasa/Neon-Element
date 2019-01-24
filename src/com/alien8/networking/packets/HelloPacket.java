package com.alien8.networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class HelloPacket extends Packet {

    protected HelloPacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
        super(PacketDirection.INCOMING, PacketType.HELLO, ipAddress, port);
    }

    public HelloPacket(InetAddress ipAddress, int port) {
        super(PacketDirection.OUTGOING, PacketType.HELLO, ipAddress, port);
    }
    
    public byte[] getRawBytes() {
        byte[] data = new byte[Packet.PACKET_BYTES_LENGTH];
        ByteBuffer buffer = this.getByteBuffer();
        buffer.get(data);
        return data;
    }
}
