package engine.entities;

import javafx.scene.shape.Shape;

public class CollisionDetection {

    public static boolean checkCollision(PhysicsObject object1, PhysicsObject object2) {
        Shape intersect = Shape.intersect(object1.getBounds(), object2.getBounds());
        return intersect.getBoundsInLocal().getWidth() != -1;
    }

    public static boolean checkCollision(Shape object1, Shape object2) {
        Shape intersect = Shape.intersect(object1, object2);
        return intersect.getBoundsInLocal().getWidth() != -1;
    }


}
