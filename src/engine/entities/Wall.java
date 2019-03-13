package engine.entities;

import engine.enums.ObjectType;
import javafx.geometry.Point2D;

/**
 * A wall in the game
 */
public class Wall extends PhysicsObject {

    /**
     * Main Constructor to set the physic object to obastacle
     */
    private Wall() {
        tag = ObjectType.OBSTACLE;
    }

    /**
     * Constructor
     *
     * @param location the top left location of this wall
     * @param width    the width of the wall expanded to the right
     * @param height   the height of the wall expanded down
     */
    public Wall(Point2D location, int width, int height) {
        this();
        this.height = height;
        this.width = width;
        this.location = location.add(width/2f,height / 2f);

    }

    /** Constructor
     * @param x top left x coordinate of the wall
     * @param y top left y coordinate of the wall
     * @param width the width of the wall expanded to the right
     * @param height the height of the wall expanded down
     */
    public Wall(double x, double y, int width, int height) {
        this();
        this.height = height;
        this.width = width;
        this.location = new Point2D(x+width/2f, y+height/2f);
    }


    @Override
    public void update() {

    }
}
