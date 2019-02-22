package engine.enumSwitches;

import engine.enums.ObjectType;

public class objectSize {
    public static int getObjectSize(ObjectType obj){
        switch(obj){
            case PLAYER:
                return 40;
            case POWERUP:
                return 20;
            case ENEMY:
                return 40;
            case OBSTACLE:
                return 50;
            default:
                return 10;
        }
    }
}
