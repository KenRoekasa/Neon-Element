package entities;

import javafx.scene.shape.Circle;

public class CollisionDetection {

    public static boolean checkCollision(PhysicsObject object1,PhysicsObject object2){
        if(object1.getBounds().intersects(object2.getBounds().getBoundsInParent())){
            return true;
        }
        return false;
    }

}
