package com.alien8.networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class HelloPacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // 
    // = 0 bytes

    protected HelloPacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
        super(PacketDirection.INCOMING, PacketType.HELLO, ipAddress, port);
    }

    public HelloPacket(InetAddress ipAddress, int port) {
        super(PacketDirection.OUTGOING, PacketType.HELLO, ipAddress, port);
    }
    
    public byte[] getRawBytes() {
        /*byte[] data = new byte[Packet.PACKET_BYTES_LENGTH];
        ByteBuffer buffer = this.getByteBuffer();
        buffer.get(data);*/
        String str = new String(new byte[] {this.getType().getId()}) + "";
        return str.getBytes();
    }
}
