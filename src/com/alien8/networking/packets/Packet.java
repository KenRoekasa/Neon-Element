package com.alien8.networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public abstract class Packet {
    
    protected static final int PACKET_BYTES_LENGTH = 16;
    
    public static enum PacketDirection { INCOMING, OUTGOING }

    public static enum PacketType {
        HELLO(0), HELLO_ACK(1);

        private int id;

        PacketType(int id) {
            this.id = id;
        }
        
        int getId() {
            return this.id;
        }

        public static PacketType getTypeFromId(int id) {
            for (PacketType t : PacketType.values()) {
                if (t.id == id) {
                    return t;
                }
            }

            return null;
        }
    }

    private PacketDirection direction;
    private PacketType type;
    private InetAddress ipAddress;
    private int port;

    protected Packet(PacketDirection direction, PacketType type, InetAddress ipAddress, int port) {
        this.direction = direction;
        this.type = type;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public abstract byte[] getRawBytes();

    public PacketDirection getDirection() {
        return this.direction;
    }

    public PacketType getType() {
        return this.type;
    }

    public InetAddress getIpAddress() {
        return this.ipAddress;
    }

    public int getPort() {
        return this.port;
    }

    ByteBuffer getByteBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(Packet.PACKET_BYTES_LENGTH);
        buffer.putInt(this.type.getId());
        return buffer;
    }

    public static Packet createFromBytes(byte[] rawData, InetAddress ipAddress, int port) {
        int id = rawData[0];
        int dataLen = rawData.length - 1;
        byte[] data = new byte[dataLen];
        System.arraycopy(rawData, 1, data, 0, dataLen);

        ByteBuffer buffer = ByteBuffer.allocate(Packet.PACKET_BYTES_LENGTH);
        buffer.put(data);

        Packet packet;
        PacketType type = PacketType.getTypeFromId(id);

        switch (type) {
            case HELLO:
                packet = new HelloPacket(buffer, ipAddress, port);
                break;
            case HELLO_ACK:
                packet = new HelloAckPacket(buffer, ipAddress, port);
            default:
                packet = null;
        }

        return packet;
    }

}
