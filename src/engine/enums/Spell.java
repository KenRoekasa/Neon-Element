package engine.enums;

import utils.InvalidEnumId;
import utils.LookupableById;

public enum Spell implements LookupableById {
    LIGHT(0), HEAVY(1), SHIELD(2);

    private byte id;

    private Spell(int id) {
        this.id = (byte) id;
    }

    public byte getId() {
        return this.id;
    }

    public static Spell getById(byte id) throws InvalidEnumId {
        return LookupableById.lookup(Spell.class, id);
    }
}
