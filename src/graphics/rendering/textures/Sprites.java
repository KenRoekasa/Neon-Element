package graphics.rendering.textures;

public enum Sprites {
    MAP("graphics/rendering/textures/map.png"),
    CURSOR("graphics/rendering/textures/pointer.png"),
    BLADE("graphics/rendering/textures/blade.png"),
    AIR("graphics/rendering/textures/air.png"),
    EARTH("graphics/rendering/textures/earth.png"),
    FIRE("graphics/rendering/textures/fire.png"),
    WATER("graphics/rendering/textures/water.png");

    private String location;

    Sprites(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
}
