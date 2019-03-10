package engine;

import engine.entities.Wall;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class MapGenerator {


    /**
     * @return the default map of the game
     */
    public static Map createMap1() {
        //Creation of the walls
        ArrayList<Wall> walls = new ArrayList<>();
        walls.add(new Wall(100, 100, 700, 100));
        walls.add(new Wall(100, 200, 100, 600));
        walls.add(new Wall(100, 1200, 100, 600));
        walls.add(new Wall(100, 1800, 700, 100));

        walls.add(new Wall(1200, 100, 700, 100));
        walls.add(new Wall(1800, 200, 100, 600));
        walls.add(new Wall(1800, 1200, 100, 600));
        walls.add(new Wall(1200, 1800, 700, 100));

        walls.add(new Wall(400, 400, 300, 300));

        walls.add(new Wall(400, 1200, 300, 300));

        walls.add(new Wall(1300, 400, 300, 300));

        walls.add(new Wall(1300, 1200, 300, 300));

        walls.add(new Wall(800, 800, 400, 400));

        //Creation of respawn points
        ArrayList<Point2D> respawnPoints = new ArrayList<Point2D>();
        respawnPoints.add(new Point2D(50,50));
        respawnPoints.add(new Point2D(50,1950));
        respawnPoints.add(new Point2D(1950, 50));
        respawnPoints.add(new Point2D(1950, 1950));

        return new Map(new Rectangle(2000, 2000), walls,respawnPoints);
    }



}
