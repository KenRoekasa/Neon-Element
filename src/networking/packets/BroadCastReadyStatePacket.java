package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class BroadCastReadyStatePacket extends Packet {

	// Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // boolean
    // 1 = 1 bytes

    private boolean ready;

    protected BroadCastReadyStatePacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
        super(PacketType.READY_STATE_BCAST, ipAddress, port);
        this.ready = getBooleanValue(buffer.get());
    }

    public BroadCastReadyStatePacket (boolean ready) {
        super(PacketType.READY_STATE_BCAST);
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
