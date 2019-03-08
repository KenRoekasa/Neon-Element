package engine;
import engine.entities.Wall;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Map {
    private Rectangle ground;
    private ArrayList<Wall> walls;
    private ArrayList<Point2D> respawnPoints;



    public Map(Rectangle ground, ArrayList<Wall> walls,ArrayList<Point2D> respawnPoints) {
        this.ground = ground;
        this.walls = walls;
        this.respawnPoints = respawnPoints;
    }

    public Rectangle getGround() {
        return ground;
    }

    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public ArrayList<Point2D> getRespawnPoints() {
        return respawnPoints;
    }
}
