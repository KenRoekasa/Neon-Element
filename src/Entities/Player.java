package Entities;

import Enums.Elements;
import javafx.geometry.Point2D;
import javafx.scene.transform.Rotate;


public class Player extends Character {


    //COOLDOWNS
    //The number of seconds for change state to go off cooldown
    private final float CHANGE_STATE_CD = 1.2f;
    // The countdown of the changestate cooldown
    private float changeStateCurrentCD;


    public Player(){
        location = new Point2D(0, 0);
        playerAngle = new Rotate(0);
        health = 100;
        movementSpeed = 10;
        isShielded = false;
        //Default Fire
        currentElement = Elements.FIRE;
        width = 20;
    }


    @Override
    public void update() { // Called every game tick, put location updates server sending etc... here
        if(health <0){
            isAlive = false;
        }else{
            isAlive = true;
        }

    }
}
