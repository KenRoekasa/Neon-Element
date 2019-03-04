package engine;
import engine.entities.Wall;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Map {
    Rectangle ground;
    ArrayList<Wall> walls;



    public Map(Rectangle ground, ArrayList<Wall> walls) {
        this.ground = ground;
        this.walls = walls;
    }




}
