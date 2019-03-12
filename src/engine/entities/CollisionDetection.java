package engine.entities;

import javafx.scene.shape.Shape;

/**
 * Collision detector between to objects
 */
public class CollisionDetection {

    /**
     * Check if 2 objects have collided with each other
     *
     * @param object1 the first physics object
     * @param object2 the second physics object
     * @return true if first object is on top of seconds object and vice versa; false otherwise
     */
    public static boolean checkCollision(PhysicsObject object1, PhysicsObject object2) {
        Shape intersect = Shape.intersect(object1.getBounds(), object2.getBounds());
        return intersect.getBoundsInLocal().getWidth() != -1;
    }

    /**
     * Check if 2 objects have collided with each other
     *
     * @param object1 the first object
     * @param object2 the second object
     * @return true if first object is on top of seconds object and vice versa; false otherwise
     */
    public static boolean checkCollision(Shape object1, Shape object2) {
        Shape intersect = Shape.intersect(object1, object2);
        return intersect.getBoundsInLocal().getWidth() != -1;
    }


}
