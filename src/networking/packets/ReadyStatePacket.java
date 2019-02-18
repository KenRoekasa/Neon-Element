package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class ReadyStatePacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // boolean
    // 1 = 1 bytes

    private boolean ready;

    protected ReadyStatePacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
        super(PacketDirection.INCOMING, PacketType.READY_STATE, ipAddress, port);
        this.ready = getBooleanValue(buffer.get());
    }

    public ReadyStatePacket(InetAddress ipAddress, int port, boolean ready) {
        super(PacketDirection.OUTGOING, PacketType.READY_STATE, ipAddress, port);
        this.ready = ready;
    }

    public boolean getReady() {
        return this.ready;
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.put(getByteValue(this.ready));
        return Packet.getBytesFromBuffer(buffer);
    }

}
