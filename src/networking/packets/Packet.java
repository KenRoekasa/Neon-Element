package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

import networking.client.ClientNetworkHandler;
import networking.server.ServerNetworkHandler;
import utils.InvalidEnumId;
import utils.LookupableById;

public abstract class Packet {

    /** The number of bytes contained in a packet including the packet ID. */
    public static final int PACKET_BYTES_LENGTH = 128;

    public static enum PacketDirection { INCOMING, OUTGOING }

    @FunctionalInterface
    private static interface PacketConstructor {
        Packet apply(ByteBuffer buffer, Sender sender) throws Exception;
    }

    public static enum PacketType implements LookupableById {
        // Client -> Server                                     // Server -> Client
        HELLO          ((byte) 0x00, HelloPacket::new),         HELLO_ACK            ((byte) 0x01, HelloAckPacket::new),
        CONNECT        ((byte) 0x02, ConnectPacket::new),       CONNECT_ACK          ((byte) 0x03, ConnectAckPacket::new),
        DISCONNECT     ((byte) 0x04, DisconnectPacket::new),    DISCONNECT_ACK       ((byte) 0x05, DisconnectAckPacket::new),
        READY_STATE    ((byte) 0x06, ReadyStatePacket::new),    // HEALTH_STATE         ((byte) 0x07),
        LOCATION_STATE ((byte) 0x08, LocationStatePacket::new), // LOCATION_STATE_ACK   ((byte) 0x09),
        ELEMENT_STATE  ((byte) 0x0A, ElementStatePacket::new),

        POWERUP        ((byte) 0x0E, PowerUpPacket::new),
        ACTION_STATE   ((byte) 0x10, ActionStatePacket::new),   // ACTION_STATE_ACK     ((byte) 0x11),

        // Broadcast from Server -> All Clients
        CONNECT_BCAST        ((byte) 0xF0, BroadCastConnectedUserPacket::new),
        DISCONNECT_BCAST     ((byte) 0xF1, BroadCastDisconnectedUserPacket::new),
        READY_STATE_BCAST    ((byte) 0xF2, BroadCastReadyStatePacket::new),
        LOCATION_STATE_BCAST ((byte) 0xF3, BroadCastLocationStatePacket::new),
        ELEMENT_STATE_BCAST  ((byte) 0xF4, BroadCastElementStatePacket::new),

        POWERUP_PICKUP_BCAST ((byte) 0xF6, BroadCastPowerUpPickUpPacket::new),
        POWERUP_STATE_BCAST  ((byte) 0xF7, BroadCastPowerUpPacket::new),
        INITIAL_STATE_BCAST  ((byte) 0xF8, BroadCastinitialGameStatePacket::new),
        ACTION_BCAST         ((byte) 0xF9, BroadcastActionPacket::new),
        GAME_START_BCAST     ((byte) 0xFE, BroadCastGameStartPacket::new),
        GAME_OVER_BCAST      ((byte) 0xFF, BroadCastGameOverPacket::new);

        private byte id;
        private PacketConstructor constructor;

        private PacketType(byte id, PacketConstructor constructor) {
            this.id = id;
            this.constructor = constructor;
        }

        public byte getId() {
            return this.id;
        }

        protected Packet create(ByteBuffer buffer, Sender sender) throws Exception {
            return this.constructor.apply(buffer, sender);
        }

        public static PacketType getById(byte id) throws InvalidEnumId {
            return LookupableById.lookup(PacketType.class, id);
        }

    }

    public static class Sender {
        private InetAddress ipAddress;
        private int port;

        protected Sender(InetAddress ipAddress, int port) {
            this.ipAddress = ipAddress;
            this.port = port;
        }

        protected InetAddress getIpAddress() {
            return this.ipAddress;
        }

        protected int getPort() {
            return this.port;
        }
    }

    public static abstract class PacketToClient extends Packet {
        protected PacketToClient(Sender sender) {
            super(sender);
        }

        protected PacketToClient() {
            super();
        }

        public abstract void handle(ClientNetworkHandler handler);
    }

    public static abstract class PacketToServer extends Packet {
        protected PacketToServer(Sender sender) {
            super(sender);
        }

        protected PacketToServer() {
            super();
        }

        public abstract void handle(ServerNetworkHandler handler);
    }

    private PacketDirection direction;

    // Only set for incoming packets
    private Sender sender;

    private Packet(Sender sender) {
        this.direction = PacketDirection.INCOMING;
        this.sender = sender;
    }

    private Packet() {
    	this.direction = PacketDirection.OUTGOING;
    }

    public abstract byte[] getRawBytes();

    public PacketDirection getDirection() {
        return this.direction;
    }

    public abstract PacketType getPacketType();

    public InetAddress getIpAddress() {
        return this.sender.getIpAddress();
    }

    public int getPort() {
        return this.sender.getPort();
    }

    protected ByteBuffer getByteBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(Packet.PACKET_BYTES_LENGTH);
        buffer.put(this.getPacketType().getId());
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

        Sender sender = new Sender(ipAddress, port);

        try {
            PacketType type = LookupableById.lookup(PacketType.class, id);

            Packet packet = type.create(buffer, sender);

            return packet;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final byte getByteValue(boolean b) {
        return (byte) (b ? 0x01 : 0x00);
    }

    public static final boolean getBooleanValue(byte b) {
        return (b == (byte) 0x01);
    }

}
