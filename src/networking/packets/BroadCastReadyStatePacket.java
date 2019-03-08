package networking.packets;

import java.nio.ByteBuffer;

import networking.packets.Packet.PacketDirection;
import networking.packets.Packet.PacketType;

public class BroadCastReadyStatePacket extends Packet {

	// Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // boolean
    // 1 = 1 bytes

    private boolean ready;

    protected BroadCastReadyStatePacket(ByteBuffer buffer) {
        super(PacketDirection.INCOMING, PacketType.READY_STATE_BCAST);
        this.ready = getBooleanValue(buffer.get());
    }

    public BroadCastReadyStatePacket (boolean ready) {
        super(PacketDirection.OUTGOING, PacketType.READY_STATE_BCAST);
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
