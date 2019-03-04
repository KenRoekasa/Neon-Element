package networking.packets;

import java.nio.ByteBuffer;

import engine.enums.Spell;
import networking.server.ServerNetworkDispatcher;

public class CastSpellPacket extends Packet.PacketToServer {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // byte
    // 1 = 1 bytes

    private Spell spell;

    protected CastSpellPacket(ByteBuffer buffer, Sender sender) throws Exception {
        super(sender);
        this.spell = Spell.getById(buffer.get());
    }

    public CastSpellPacket(Spell spell) {
        super();
        this.spell = spell;
    }

    public PacketType getPacketType() {
       return PacketType.CAST_SPELL;
    }

    public Spell getSpell() {
        return this.spell;
    }

    @Override
    public void handle(ServerNetworkDispatcher dispatcher) {
        // TODO handle packet
    }

    @Override
    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        //this method getByteBuffer() already places the id of the spell into the first position of the buffer array.
        //buffer.put(this.spell.getId());
        return Packet.getBytesFromBuffer(buffer);
    }

}
