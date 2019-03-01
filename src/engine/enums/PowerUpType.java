package engine.enums;

public enum PowerUpType {
    HEAL(1), SPEED(2), DAMAGE(3);

    private byte id;

    private PowerUpType(int id) {
        this.id = (byte) id;
    }

    public byte getId() {
        return this.id;
    }

    public static PowerUpType getById(byte id) {
        for (PowerUpType t : PowerUpType.values()) {
            if (t.id == id) {
                return t;
            }
        }
        return null;
    }

}
