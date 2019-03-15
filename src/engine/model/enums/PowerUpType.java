package engine.model.enums;

import utils.InvalidEnumId;
import utils.LookupableById;

/**
 * The type that a powerup can be
 */
public enum PowerUpType implements LookupableById {
    HEAL(1), SPEED(2), DAMAGE(3);

    private byte id;

    private PowerUpType(int id) {
        this.id = (byte) id;
    }

    public byte getId() {
        return this.id;
    }

    public static PowerUpType getById(byte id) throws InvalidEnumId {
        return LookupableById.lookup(PowerUpType.class, id);
    }

}
