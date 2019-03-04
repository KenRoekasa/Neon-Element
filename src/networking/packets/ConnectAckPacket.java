package networking.packets;

import java.nio.ByteBuffer;

public class ConnectAckPacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // int + Status
    // 4   + 1      = 5 bytes

    private int playerId;
    private Status status;

    public static enum Status {
        SUC_CONNECTED((byte) 0x00),
        ERR_GAME_STARTED((byte) 0x01),
        ERR_MAX_PLAYERS((byte) 0x02);

        private byte id;

        private Status(byte id) {
            this.id = id;
        }

        protected byte getId() {
            return this.id;
        }

        public static Status getTypeFromId(byte id) {
            for (Status t : Status.values()) {
                if (t.id == id) {
                    return t;
                }
            }
            return null;
        }
    }

    protected ConnectAckPacket(ByteBuffer buffer, Sender sender) {
        super(sender);
        this.playerId = buffer.getInt();
        this.status = Status.getTypeFromId(buffer.get());
    }

    public ConnectAckPacket(int playerId, Status status) {
        super();
        this.playerId = playerId;
        this.status = status;
    }

    public PacketType getPacketType() {
       return PacketType.CONNECT_ACK;
    }

    public int getId() {
        return this.playerId;
    }

    public boolean getAllowed() {
        return this.status == Status.SUC_CONNECTED;
    }

    public Status getStatus() {
        return this.status;
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.putInt(this.playerId);
        buffer.put(this.status.getId());
        return Packet.getBytesFromBuffer(buffer);
    }

}
