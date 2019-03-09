package networking.packets;

import java.nio.ByteBuffer;

import engine.enums.PowerUpType;
import networking.client.ClientNetworkHandler;

public class PowerUpBroadcast extends Packet.PacketToClient {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // int + double + double + byte
    // 4   + 8      + 8      + 1    = 20 bytes

	private int powerUpId;
    private double x;
    private double y;
    private PowerUpType type;

	protected PowerUpBroadcast(ByteBuffer buffer, Sender sender) throws Exception {
		super(sender);
        this.powerUpId = buffer.getInt();
        this.x = buffer.getDouble();
        this.y = buffer.getDouble();
        this.type = PowerUpType.getById(buffer.get());
	}

	public PowerUpBroadcast(int powerUpId, double x, double y, PowerUpType type) {
        super();
        this.powerUpId = powerUpId;
        this.x = x;
        this.y = y;
        this.type = type;
    }

    @Override
    public PacketType getPacketType() {
       return PacketType.POWERUP_STATE_BCAST;
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
    public void handle(ClientNetworkHandler handler) {
        handler.receivePowerUpBroadcast(this);
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
