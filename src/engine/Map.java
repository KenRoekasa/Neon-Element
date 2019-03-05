package engine;
import engine.entities.Wall;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Map {
    private Rectangle ground;
    private ArrayList<Wall> walls;



    public Map(Rectangle ground, ArrayList<Wall> walls) {
        this.ground = ground;
        this.walls = walls;
    }

    public Rectangle getGround() {
        return ground;
    }

    public ArrayList<Wall> getWalls() {
        return walls;
    }
}
