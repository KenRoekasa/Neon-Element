package engine.model.enums;

import graphics.rendering.textures.Sprites;

/**
 * The Elements that the player can change into
 */
public enum Elements {
    //subject to change
    FIRE(0), WATER(1), EARTH(2), AIR(3);

    private byte id;


    Elements(int id) {
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

    public static Sprites getSprite(Elements element) {
        switch (element) {
            case FIRE:
                return Sprites.FIRE;

            case AIR:
                return Sprites.AIR;

            case EARTH:
                return Sprites.EARTH;

            case WATER:
                return Sprites.WATER;

            default:
                return Sprites.WATER;

        }
    }

}
