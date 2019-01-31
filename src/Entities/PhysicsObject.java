package Entities;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public abstract class PhysicsObject {
    protected int width;
    protected Point2D location;

    // Update function will run on all physics object in the game, will constantly be called.
    // Add like send to server / location update in here
    public abstract void update();

    public Point2D getLocation() {
        return location;
    }

    public int getWidth() {
        return width;
    }

    public Rectangle getBounds() {
        return new Rectangle(location.getX(), location.getY(), width, width);
    }
}
