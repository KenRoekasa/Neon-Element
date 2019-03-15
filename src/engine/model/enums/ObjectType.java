package engine.model.enums;

/**
 * The type that a physic object can be
 */
public enum ObjectType {
    ENEMY(60),
    OBSTACLE(100),
    PLAYER(60),
    POWERUP(20);

    private final int size;

    ObjectType(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
