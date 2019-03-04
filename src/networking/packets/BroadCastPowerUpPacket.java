package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

import engine.enums.PowerUpType;

public class BroadCastPowerUpPacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // int + double + double + byte
    // 4   + 8      + 8      + 1    = 20 bytes

	private int powerUpId;
    private double x;
    private double y;
    private PowerUpType type;

	protected BroadCastPowerUpPacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
		super(PacketType.POWERUP_STATE_BCAST, ipAddress, port);
        this.powerUpId = buffer.getInt();
        this.x = buffer.getDouble();
        this.y = buffer.getDouble();
        this.type = PowerUpType.getById(buffer.get());
	}

	public BroadCastPowerUpPacket(int powerUpId, double x, double y, PowerUpType type) {
        super(PacketType.CAST_SPELL);
        this.powerUpId = powerUpId;
        this.x = x;
        this.y = y;
        this.type = type;
    }

	public int getPowerUpId() {
        return powerUpId;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public PowerUpType getPowerUpType() {
        return this.type;
    }

    @Override
	public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.putInt(this.powerUpId);
        buffer.putDouble(this.x);
        buffer.putDouble(this.y);
        buffer.put(this.type.getId());
        return Packet.getBytesFromBuffer(buffer);
	}

}
