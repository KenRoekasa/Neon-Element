package Enums;

public enum Spell {
    LIGHT(0), HEAVY(1), SHIELD(2);

    private byte id;

    private Spell(int id) {
        this.id = (byte) id;
    }

    public byte getId() {
        return this.id;
    }

    public static Spell getById(byte id) {
        for (Spell t : Spell.values()) {
            if (t.id == id) {
                return t;
            }
        }
        return null;
    }
}
