package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

import engine.enums.Spell;

public class CastSpellPacket extends Packet {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // byte
    // 1 = 1 bytes

    private Spell spell;

    protected CastSpellPacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
        super(PacketDirection.INCOMING, PacketType.CAST_SPELL, ipAddress, port);
        this.spell = Spell.getById(buffer.get());
    }

    public CastSpellPacket(InetAddress ipAddress, int port, Spell spell) {
        super(PacketDirection.OUTGOING, PacketType.CAST_SPELL, ipAddress, port);
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
