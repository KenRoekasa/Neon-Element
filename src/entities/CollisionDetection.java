package entities;

import javafx.geometry.Bounds;
import javafx.scene.shape.Circle;

public class CollisionDetection {

    public static boolean checkCollision(PhysicsObject object1,PhysicsObject object2){
        if(object1.getBounds().intersects(object2.getBounds().getBoundsInParent())){
            return true;
        }
        return false;
    }
    public static boolean checkCollision(Bounds object1, Bounds object2){
        if(object1.intersects(object2)){
            return true;
        }
        return false;
    }

}
