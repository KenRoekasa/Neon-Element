package engine.enums;

public enum Action {
    IDLE(0), LIGHT(1), HEAVY(2), BLOCK(3), CHARGE(4);

    private byte id;

    private Action(int id) {
        this.id = (byte) id;
    }

    public byte getId() {
        return this.id;
    }

    public static Action getById(byte id) {
        for (Action t : Action.values()) {
            if (t.id == id) {
                return t;
            }
        }
        return null;
    }

}
