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

    /**
     * Incoming if the packet has been received from the network.
     * Outgoing if the packet has been created to be sent.
     */
    public static enum PacketDirection { INCOMING, OUTGOING }

    @FunctionalInterface
    private static interface PacketConstructor {
        Packet apply(ByteBuffer buffer, Sender sender) throws Exception;
    }

    public static enum PacketType implements LookupableById {
        ACTION_BCAST((byte) 0xF9, ActionStateBroadcast::new), ACTION_STATE((byte) 0x10, ActionStatePacket::new), // ACTION_STATE_ACK     ((byte) 0x11),
        CONNECT((byte) 0x02, ConnectPacket::new), CONNECT_ACK((byte) 0x03, ConnectAckPacket::new),
        CONNECT_BCAST((byte) 0xF0, ConnectBroadcast::new)// Broadcast from Server -> All Clients
        ,
        DISCONNECT((byte) 0x04, DisconnectPacket::new),

        DISCONNECT_ACK((byte) 0x05, DisconnectAckPacket::new),
        DISCONNECT_BCAST((byte) 0xF1, DisconnectBroadcast::new),
        ELEMENT_STATE((byte) 0x0A, ElementStatePacket::new),

        ELEMENT_STATE_BCAST((byte) 0xF4, ElementStateBroadcast::new),
        HEALTH_STATE_BCAST((byte) 0xF5, HealthStateBroadcast::new),

        HELLO((byte) 0x00, HelloPacket::new)// Client -> Server                                     // Server -> Client
        ,

        HELLO_ACK((byte) 0x01, HelloAckPacket::new),
        INITIAL_STATE_BCAST((byte) 0xF8, InitialGameStateBroadcast::new),
        LOCATION_STATE((byte) 0x08, LocationStatePacket::new), // LOCATION_STATE_ACK   ((byte) 0x09),
        LOCATION_STATE_BCAST((byte) 0xF3, LocationStateBroadcast::new),
        SCORE_BCAST((byte) 0x0B, ScoreBroadcast::new),

        POWERUP((byte) 0x0E, PowerUpPacket::new),
        POWERUP_PICKUP_BCAST((byte) 0xF6, PowerUpPickUpBroadcast::new),
        POWERUP_STATE_BCAST((byte) 0xF7, PowerUpBroadcast::new),
        GAME_START_BCAST((byte) 0xFE, GameStartBroadcast::new),
        GAME_OVER_BCAST((byte) 0xFF, GameOverBroadcast::new),


        READY_STATE((byte) 0x06, ReadyStatePacket::new), // HEALTH_STATE         ((byte) 0x07),
        READY_STATE_BCAST((byte) 0xF2, ReadyStateBroadcast::new);

        private byte id;
        private PacketConstructor constructor;

        private PacketType(byte id, PacketConstructor constructor) {
            this.id = id;
            this.constructor = constructor;
        }

        /**
         * Get the unique identifier of the type.
         */
        public byte getId() {
            return this.id;
        }

        /**
         * Create the packet the enum represents.
         */
        protected Packet create(ByteBuffer buffer, Sender sender) throws Exception {
            return this.constructor.apply(buffer, sender);
        }

        /**
         * Lookup a {@link PacketType} by the ID.
         */
        public static PacketType getById(byte id) throws InvalidEnumId {
            return LookupableById.lookup(PacketType.class, id);
        }

    }

    /**
     * Container class for IP address and port of sender.
     */
    public static class Sender {
        /** Originating IP address. */
        private InetAddress ipAddress;
        /** Originating port */
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

    /**
     * A Packet sent from Server to Client.
     */
    public static abstract class PacketToClient extends Packet {
        /**
         * Incoming packet from {@link Sender}.
         *
         * @param sender
         */
        protected PacketToClient(Sender sender) {
            super(sender);
        }

        /**
         * Outgoing packet.
         */
        protected PacketToClient() {
            super();
        }

        /**
         * Handle the incoming packet to the client.
         *
         * @param handler The handler class for the packet.
         */
        public abstract void handle(ClientNetworkHandler handler);
    }

    /**
     * A Packet sent from Client to Server.
     */
    public static abstract class PacketToServer extends Packet {
        /**
         * Incoming packet from {@link Sender}.
         *
         * @param sender
         */
        protected PacketToServer(Sender sender) {
            super(sender);
        }

        /**
         * Outgoing packet.
         */
        protected PacketToServer() {
            super();
        }

        /**
         * Handle the incoming packet to the server.
         *
         * @param handler The handler class for the packet.
         */
        public abstract void handle(ServerNetworkHandler handler);
    }

    private PacketDirection direction;

    /**
     * The sender of the packet. Only set on Packets with {@link PacketDirection} incoming.
     */
    private Sender sender;

    /**
     * Incoming packet from {@link Sender}.
     *
     * @param sender
     */
    private Packet(Sender sender) {
        this.direction = PacketDirection.INCOMING;
        this.sender = sender;
    }

    /**
     * Outgoing packet.
     */
    private Packet() {
        this.direction = PacketDirection.OUTGOING;
    }

    /**
     * Get the raw bytes of the packet to be sent over the network.
     * This includes the identifier of the {@link PacketType}.
     *
     * Implementors must use the following template for their implementation:
     * <pre>
     *     @Override
     *     public byte[] getRawBytes() {
     *         ByteBuffer buffer = this.getByteBuffer();
     *         // Put data into the buffer here...
     *         return Packet.getBytesFromBuffer(buffer);
     *     }
     * </pre>
     *
     * @return Array of bytes for the datagram packet.
     */
    public abstract byte[] getRawBytes();

    /**
     * Get the direction of the packet.
     *
     * @return Direction of packet.
     */
    public PacketDirection getDirection() {
        return this.direction;
    }

    /**
     * Get the type of the packet.
     *
     * Implementors must return a unique {@link PacketType} enum value.
     * This enum must also point to the constructor of the implementor.
     *
     * @return The packet type.
     */
    public abstract PacketType getPacketType();

    /**
     * Get the IP address of the sender. Only set for {@link PacketDirection#INCOMING} packets.
     *
     * @return The originating IP address of the packet.
     */
    public InetAddress getIpAddress() {
        return this.sender.getIpAddress();
    }

    /**
     * Get the port of the sender. Only set for {@link PacketDirection#INCOMING} packets.
     *
     * @return The originating port of the packet.
     */
    public int getPort() {
        return this.sender.getPort();
    }

    /**
     * Create a byte buffer for the {@link #getRawBytes()} method.
     *
     * @return A buffer of size {@link #PACKET_BYTES_LENGTH} with the first byte set to the packet type ID.
     */
    protected ByteBuffer getByteBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(Packet.PACKET_BYTES_LENGTH);
        buffer.put(this.getPacketType().getId());
        return buffer;
    }

    /**
     * Get the bytes stored in the buffer.
     *
     * @param buffer The buffer to extract the byte array from.
     * @return Byte array of bytes stored in the buffer
     */
    protected static byte[] getBytesFromBuffer(ByteBuffer buffer) {
        return buffer.array();
    }

    /**
     * Create a Packet from bytes received from the network.
     *
     * @param rawData Bytes from the network.
     * @param ipAddress Originating IP address.
     * @param port Originating port.
     * @return The reconstructed {@link PacketDirection#INCOMING} Packet.
     */
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

    /**
     * Get the byte value of the boolean
     *
     * @param b The boolean.
     * @return 0x01 if true, 0x00 if false.
     */
    public static final byte getByteValue(boolean b) {
        return (byte) (b ? 0x01 : 0x00);
    }

    /**
     * Get the boolean value of "boolean" byte.
     *
     * @param b The byte.
     * @return true if 0x01, else false.
     */
    public static final boolean getBooleanValue(byte b) {
        return (b == (byte) 0x01);
    }

}
