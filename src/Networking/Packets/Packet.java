package Networking.Packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public abstract class Packet {

    /** The number of bytes contained in a packet including the packet ID. */
    public static final int PACKET_BYTES_LENGTH = 16;

    public static enum PacketDirection { INCOMING, OUTGOING }

    public static enum PacketType {
        HELLO((byte) 0x00), HELLO_ACK((byte) 0x01);

        private byte id;

        PacketType(byte id) {
            this.id = id;
        }
        
        
        byte getId() {
            return this.id;
        }

        public static PacketType getTypeFromId(byte id) {
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
        buffer.put(this.type.getId());
        return buffer;
    }

    public static Packet createFromBytes(byte[] rawData, InetAddress ipAddress, int port) {
        byte id = rawData[0];
        int dataLen = rawData.length - 1;
        byte[] data = new byte[dataLen];
        System.arraycopy(rawData, 1, data, 0, dataLen);

        String str = new String(data).trim();
        String[] parts = str.split(";");

        Packet packet;
        PacketType type = PacketType.getTypeFromId(id);
        
        System.out.println("" + ipAddress + ":" + port + " --> " + type + " " + str);

        switch (type) {
            case HELLO:
                packet = new HelloPacket(parts, ipAddress, port);
                break;
            case HELLO_ACK:
                packet = new HelloAckPacket(parts, ipAddress, port);
                break;
            default:
                packet = null;
        }

        return packet;
    }

}
