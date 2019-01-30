package Enums;

public enum PlayerStates {
    //subject to change
    FIRE(0), WATER(1), EARTH(2), AIR(3);

    private byte id;

    private PlayerStates(int id) {
        this.id = (byte) id;
    }

    public byte getId() {
        return this.id;
    }

    public static PlayerStates getById(byte id) {
        for (PlayerStates t : PlayerStates.values()) {
            if (t.id == id) {
                return t;
            }
        }
        return null;
    }
}