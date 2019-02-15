package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public abstract class Packet {

    /** The number of bytes contained in a packet including the packet ID. */
    public static final int PACKET_BYTES_LENGTH = 64;

    public static enum PacketDirection { INCOMING, OUTGOING }

    public static enum PacketType {
        // Client -> Server                 // Server -> Client
        HELLO                ((byte) 0x00), HELLO_ACK            ((byte) 0x01),
        CONNECT              ((byte) 0x02), CONNECT_ACK          ((byte) 0x03),
        DISCONNECT           ((byte) 0x04), DISCONNECT_ACK       ((byte) 0x05),
        READY_STATE          ((byte) 0x06), HEALTH_STATE         ((byte) 0x07),
        LOCATION_STATE       ((byte) 0x08), LOCATION_STATE_ACK   ((byte) 0x09),
        ELEMENT_STATE        ((byte) 0x0A),
        CAST_SPELL           ((byte) 0x0C),
        POWERUP              ((byte) 0x0E),

        // Broadcast from Server -> All Clients
        CONNECT_BCAST        ((byte) 0xF0), DISCONNECT_BCAST     ((byte) 0xF1),
        READY_STATE_BCAST    ((byte) 0xF2), LOCATION_STATE_BCAST ((byte) 0xF3),
        ELEMENT_STATE_BCAST  ((byte) 0xF4), CAST_SPELL_BCAST     ((byte) 0xF5),
        POWERUP_PICKUP_BCAST ((byte) 0xF6), POWERUP_STATE_BCAST  ((byte) 0xF7),
        GAME_START_BCAST     ((byte) 0xFE), GAME_OVER_BCAST      ((byte) 0xFF);

        private byte id;

        private PacketType(byte id) {
            this.id = id;
        }

        protected byte getId() {
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
    
    protected Packet(PacketDirection direction, PacketType type) {
    	this.direction = direction;
        this.type = type;
       
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

    protected ByteBuffer getByteBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(Packet.PACKET_BYTES_LENGTH);
        buffer.put(this.type.getId());
        return buffer;
    }

    protected static byte[] getBytesFromBuffer(ByteBuffer buffer) {
        return buffer.array();
    }

    public static Packet createFromBytes(byte[] rawData, InetAddress ipAddress, int port) {
        byte id = rawData[0];
        int dataLen = rawData.length - 1;
        byte[] data = new byte[dataLen];
        System.arraycopy(rawData, 1, data, 0, dataLen);

        ByteBuffer buffer = ByteBuffer.allocate(Packet.PACKET_BYTES_LENGTH);
        buffer.put(data);
        buffer.flip();

        Packet packet;
        PacketType type = PacketType.getTypeFromId(id);

        System.out.println("" + ipAddress + ":" + port + " --> " + type);

        switch (type) {
            default:
                packet = null;
                break;
            case HELLO:
                packet = new HelloPacket(buffer, ipAddress, port);
                break;
            case HELLO_ACK:
                packet = new HelloAckPacket(buffer, ipAddress, port);
                break;
            case CONNECT:
                packet = new ConnectPacket(buffer, ipAddress, port);
                break;
            case CONNECT_ACK:
                packet = new ConnectAckPacket(buffer, ipAddress, port);
                break;
            case LOCATION_STATE:
                packet = new LocationStatePacket(buffer, ipAddress, port);
                break;
            case CONNECT_BCAST:
            		packet = new BroadCastConnectedUserPacket(buffer);
            		break;
            case DISCONNECT_BCAST:
            		packet = new BroadCastDisconnectedUserPacket(buffer);
            		break;
            case READY_STATE_BCAST:
            		packet = new BroadCastReadyStatePacket(buffer);
            		break;
            case LOCATION_STATE_BCAST:
            		packet = new BroadCastLocationStatePacket(buffer);
            		break;
            case ELEMENT_STATE_BCAST:
            		packet = new BroadCastElementStatePacket(buffer);
            		break;
            case CAST_SPELL_BCAST:
            		packet = new BroadCastCastSpellPacket(buffer);
            		break;
            case POWERUP_PICKUP_BCAST:
            		packet = new BroadCastPowerUpPickUpPacket(buffer);
            		break;
            case POWERUP_STATE_BCAST:
            		packet = new BroadCastPowerUpPacket(buffer);
            		break;
            case GAME_START_BCAST:
            		packet = new BroadCastGameStartPacket(buffer);
            		break;
            case GAME_OVER_BCAST:
            		packet = new BroadCastGameOverPacket(buffer);
            		break;
        }

        return packet;
    }

    public static final byte getByteValue(boolean b) {
        return (byte) (b ? 0x01 : 0x00);
    }

    public static final boolean getBooleanValue(byte b) {
        return (b == (byte) 0x01);
    }

}
