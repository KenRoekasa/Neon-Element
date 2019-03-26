package graphics.rendering.textures;

/**
 *  The available sprites
 *  <li>{@link #MAP}</li>
 *  <li>{@link #POINTER}</li>
 *  <li>{@link #BLADE}</li>
 */
public enum Sprites {

    /**
     *  The blade sprite
     */
    BLADE("graphics/rendering/textures/orangeblade.png"),

    /**
     * The map sprite
     */
    MAP("graphics/rendering/textures/map.png"),

    /**
     *  The pointer sprite
     */
    POINTER("graphics/rendering/textures/bluepointer.png"),

    /**
     * The crown sprite
     */
    CROWN("graphics/userInterface/resources/icons/star-icon.png");

    /**
     * The location of the sprite
     */
    private String location;

    /**
     * Sprite enum constructor
     * @param location The location of the sprite
     */
    Sprites(String location) {
        this.location = location;
    }

    /**
     * Gets the location of a sprite
     * @return The location of a sprite
     */
    public String getLocation() {
        return location;
    }
}
