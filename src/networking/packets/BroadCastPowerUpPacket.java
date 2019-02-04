package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

import entities.PowerUp;
import enums.Elements;
import enums.Spell;
import networking.packets.Packet.PacketDirection;
import networking.packets.Packet.PacketType;

public class BroadCastPowerUpPacket extends Packet {

	private PowerUp powerUp;
	protected BroadCastPowerUpPacket(ByteBuffer buffer) {
		super(PacketDirection.INCOMING, PacketType.POWERUP_STATE_BCAST);
        //this.powerUp = PowerUp.getById(buffer.get());

		// TODO Auto-generated constructor stub
	}

	public BroadCastPowerUpPacket(PowerUp powerUp) {
        super(PacketDirection.OUTGOING, PacketType.CAST_SPELL);
        this.powerUp = powerUp;
    }

	@Override
	public byte[] getRawBytes() {
		// TODO Auto-generated method stub
		return null;
	}

}
