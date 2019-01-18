package Grid;

public class TileTypes {
    public enum Types {
        GRASS,
        ROCK
    }

    public static Types getType(int value) {
        Types type = Types.values()[value];
        return type;
    }

}
