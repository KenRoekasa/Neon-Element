package entities;

import java.util.ArrayList;

public class CollisionDetection {

    public boolean checkCollision(PhysicsObject object1,PhysicsObject object2){
        if(object1.getBounds().intersects(object2.getBounds().getBoundsInParent())){
            return true;
        }
        return false;
    }

}
