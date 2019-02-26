package engine.enums;

public enum ObjectType {
    PLAYER(60),
    ENEMY(60),
    POWERUP(20),
    OBSTACLE(100);

    private final int size;

    ObjectType(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
