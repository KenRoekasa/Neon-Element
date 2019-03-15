package networking.packets;

import java.nio.ByteBuffer;

import engine.model.enums.Spell;

//This is a way to hold the information
public class BroadCastCastSpellPacket extends Packet {
	Spell spell;

	protected BroadCastCastSpellPacket(ByteBuffer buffer) {
        super(PacketDirection.INCOMING, PacketType.CAST_SPELL_BCAST);
        this.spell = Spell.getById(buffer.get());
    }

    public BroadCastCastSpellPacket(Spell spell) {
        super(PacketDirection.OUTGOING, PacketType.CAST_SPELL_BCAST);
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
