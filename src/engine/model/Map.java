package engine.model;
import engine.entities.Wall;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * The map that contains the ground,walls and respawn points
 */
public class Map {
    /**
     * A area the player can traverse
     */
    private Rectangle ground;

    private ArrayList<Wall> walls;
    /**
     * The points where the player will spawn and respawn from
     */
    private ArrayList<Point2D> respawnPoints;



    public Map(Rectangle ground, ArrayList<Wall> walls,ArrayList<Point2D> respawnPoints) {
        this.ground = ground;
        this.walls = walls;
        this.respawnPoints = respawnPoints;
    }

    public double getWidth() {
        return this.ground.getWidth();
    }

    public double getHeight() {
        return this.ground.getHeight();
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
