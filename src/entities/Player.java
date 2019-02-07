package entities;

import enumSwitches.objectSize;
import enums.Elements;
import enums.ObjectType;
import javafx.geometry.Point2D;
import javafx.scene.transform.Rotate;

import static entities.CooldownValues.*;


public class Player extends Character {


    //COOLDOWNS
    //The number of seconds for change state to go off cooldown
    private final float CHANGE_STATE_CD = 1.2f;
    // The countdown of the changestate cooldown
    private float changeStateCurrentCD;


    public Player(ObjectType type) {
        location = new Point2D(0, 0);
        playerAngle = new Rotate(0);
        health = getMAX_HEALTH();
        movementSpeed = 5;
        isShielded = false;
        //Default Fire
        currentElement = Elements.FIRE;
        width = objectSize.getObjectSize(type);
        tag = type;
        for(int i = 0; i < timerArray.length; i++){
            timerArray[i] = System.currentTimeMillis() - 10*1000;
        }
    }


    @Override
    public void update() { // Called every game tick, put location updates server sending etc... here
        if (health < 0) {
            isAlive = false;
        } else {
            isAlive = true;
        }

    }
}
