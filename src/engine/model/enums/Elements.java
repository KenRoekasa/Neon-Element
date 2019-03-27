package engine.model.enums;

import utils.InvalidEnumId;
import utils.LookupableById;
import graphics.rendering.textures.Sprites;

/**
 * The Elements that the player can change into
 */
public enum Elements implements LookupableById {
    //subject to change
    FIRE(0), WATER(1), EARTH(2), AIR(3);

    private byte id;


    Elements(int id) {
        this.id = (byte) id;
    }

    public byte getId() {
        return this.id;
    }

    public static Elements getById(byte id) throws InvalidEnumId {
        return LookupableById.lookup(Elements.class, id);
    }


}
