package engine.entities;

import engine.model.enums.ObjectType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * Any in game object that can be collided with
 */
public abstract class PhysicsObject {
    protected int width;
    protected int height;
    protected Point2D location;
    /**
     * The type of object this object is
     */
    protected ObjectType tag;

    /**
     * Is called every frame
     */
    public abstract void update();

    public Point2D getLocation() {
        return location;
    }

    public int getWidth() {
        return width;
    }

    public Shape getBounds() {
        return new Rectangle(location.getX() - width / 2f, location.getY() - height / 2f, width, height);
    }

    public ObjectType getTag() {
        return tag;
    }

    public int getHeight() {
        return height;
    }
}
