package engine.entities;

import javafx.geometry.Bounds;

public class CollisionDetection {

    public static boolean checkCollision(PhysicsObject object1,PhysicsObject object2){
        return object1.getBounds().intersects(object2.getBounds().getBoundsInParent());
    }
    public static boolean checkCollision(Bounds object1, Bounds object2){
        return object1.intersects(object2);
    }

}
