package engine.enums;

import utils.InvalidEnumId;
import utils.LookupableById;

public enum Action implements LookupableById {
    IDLE(0), LIGHT(1), HEAVY(2), BLOCK(3), CHARGE(4);

    private byte id;

    private Action(int id) {
        this.id = (byte) id;
    }

    public byte getId() {
        return this.id;
    }

    public static Action getById(byte id) throws InvalidEnumId {
        return LookupableById.lookup(Action.class, id);
    }

}
