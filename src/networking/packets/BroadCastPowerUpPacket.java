package networking.packets;

import java.nio.ByteBuffer;

public class BroadCastPowerUpPacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // int + double + double
    // 4   + 8      + 8      = 20 bytes

	private int powerUpId;
    private double x;
    private double y;

	protected BroadCastPowerUpPacket(ByteBuffer buffer) {
		super(PacketDirection.INCOMING, PacketType.POWERUP_STATE_BCAST);
        this.powerUpId = buffer.getInt();
        this.x = buffer.getDouble();
        this.y = buffer.getDouble();
	}

	public BroadCastPowerUpPacket(int powerUpId, double x, double y) {
        super(PacketDirection.OUTGOING, PacketType.CAST_SPELL);
        this.powerUpId = powerUpId;
        this.x = x;
        this.y = y;
    }

	@Override
	public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.putInt(this.powerUpId);
        buffer.putDouble(this.x);
        buffer.putDouble(this.y);
        return Packet.getBytesFromBuffer(buffer);
	}

}
