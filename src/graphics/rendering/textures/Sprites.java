package graphics.rendering.textures;

public enum Sprites {
    MAP("graphics/rendering/textures/map.png"),
    CURSOR("graphics/rendering/textures/pointer.png"),
    BLADE("graphics/rendering/textures/blade.png");

    private String location;

    Sprites(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
}
