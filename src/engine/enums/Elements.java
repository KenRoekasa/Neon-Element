package engine.enums;

public enum Elements {
    //subject to change
    FIRE(0), WATER(1), EARTH(2), AIR(3);

    private byte id;

    private Elements(int id) {
        this.id = (byte) id;
    }

    public byte getId() {
        return this.id;
    }

    public static Elements getById(byte id) {
        for (Elements t : Elements.values()) {
            if (t.id == id) {
                return t;
            }
        }
        return null;
    }
}
