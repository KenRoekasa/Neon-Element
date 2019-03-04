package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

import engine.enums.Spell;

//This is a way to hold the information
public class BroadCastCastSpellPacket extends Packet {
	Spell spell;

	protected BroadCastCastSpellPacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
        super(PacketType.CAST_SPELL_BCAST, ipAddress, port);
        this.spell = Spell.getById(buffer.get());
    }

    public BroadCastCastSpellPacket(Spell spell) {
        super(PacketType.CAST_SPELL_BCAST);
        this.spell = spell;
    }

    public Spell getSpell() {
        return this.spell;
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        //this method getByteBuffer() already places the id of the spell into the first position of the buffer array.
        //buffer.put(this.spell.getId());
        return Packet.getBytesFromBuffer(buffer);
    }

}
