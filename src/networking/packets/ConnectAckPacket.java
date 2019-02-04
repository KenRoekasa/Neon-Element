package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

import networking.packets.Packet.PacketType;

public class ConnectAckPacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // Status
    // 1 = 1 bytes

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

    protected ConnectAckPacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
        super(PacketDirection.INCOMING, PacketType.CONNECT_ACK, ipAddress, port);
        this.status = Status.getTypeFromId(buffer.get());
    }

    public ConnectAckPacket(Status status, InetAddress ipAddress, int port) {
        super(PacketDirection.OUTGOING, PacketType.CONNECT_ACK, ipAddress, port);
        this.status = status;
    }

    public boolean getAllowed() {
        return this.status == Status.SUC_CONNECTED;
    }

    public Status getStatus() {
        return this.status;
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.put(this.status.getId());
        return Packet.getBytesFromBuffer(buffer);
    }

}
