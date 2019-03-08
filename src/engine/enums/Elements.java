package engine.enums;

import utils.InvalidEnumId;
import utils.LookupableById;
import graphics.rendering.textures.Sprites;

public enum Elements implements LookupableById {
    //subject to change
    FIRE(0), WATER(1), EARTH(2), AIR(3);

    private byte id;

    private Elements(int id) {
        this.id = (byte) id;
    }

    public byte getId() {
        return this.id;
    }

    public static Elements getById(byte id) throws InvalidEnumId {
        return LookupableById.lookup(Elements.class, id);
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
