package Grid;

import javafx.scene.image.Image;

public class Tile {
    private final int TILEWIDTH = 1;

    private final int TILEHEIGHT = 1;
    private int tileType;

    public void Tile(int type) {
        tileType = type;
    }

    public int getTILEWIDTH() {
        return TILEWIDTH;
    }

    public int getTILEHEIGHT() {
        return TILEHEIGHT;
    }

    public int getTileType() {
        return tileType;
    }

}

