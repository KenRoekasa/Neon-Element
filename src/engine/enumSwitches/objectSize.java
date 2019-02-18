package engine.enumSwitches;

import engine.enums.ObjectType;

public class objectSize {
    public static int getObjectSize(ObjectType obj){
        switch(obj){
            case PLAYER:
                return 30;
            case POWERUP:
                return 15;
            case ENEMY:
                return 30;
            case OBSTACLE:
                return 50;
            default:
                return 10;
        }
    }
}
