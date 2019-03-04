package engine.enums;

import utils.LookupableById;

public enum PowerUpType implements LookupableById {
    HEAL(1), SPEED(2), DAMAGE(3);

    private byte id;

    private PowerUpType(int id) {
        this.id = (byte) id;
    }

    public byte getId() {
        return this.id;
    }

    public static PowerUpType getById(byte id) {
        return LookupableById.lookup(PowerUpType.class, id);
    }

}
