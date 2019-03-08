package engine.entities;

import engine.enums.ObjectType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public abstract class PhysicsObject {
    protected int width;
    protected int height;
    //Top left point of the object
    protected Point2D location;
    protected ObjectType tag;

    // Update function will run on all physics object in the game, will constantly be called.
    // Add like send to server / location update in here
    public abstract void update();

    public Point2D getLocation() {
        return location;
    }

    public int getWidth() {
        return width;
    }

    public Shape getBounds() {
        return new Rectangle(location.getX()-width/2f, location.getY()-height/2f,width, height);
    }

    public ObjectType getTag() {
        return tag;
    }

    public int getHeight() {
        return height;
    }
}
