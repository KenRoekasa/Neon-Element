package networking.packets;

import java.nio.ByteBuffer;

public class ReadyStatePacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // boolean
    // 1 = 1 bytes

    private boolean ready;

    protected ReadyStatePacket(ByteBuffer buffer, Sender sender) {
        super(PacketType.READY_STATE, sender);
        this.ready = getBooleanValue(buffer.get());
    }

    public ReadyStatePacket(boolean ready) {
        super(PacketType.READY_STATE);
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
