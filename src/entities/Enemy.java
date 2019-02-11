package entities;

import enumSwitches.objectSize;
import enums.Elements;
import enums.ObjectType;
import javafx.geometry.Point2D;
import javafx.scene.transform.Rotate;

public class Enemy extends Character {

    public Enemy() {
        location = new Point2D(10, 100);
        playerAngle = new Rotate(0);
        health = 100;
        movementSpeed = 5;
        isShielded = false;
        //Default Fire
        currentElement = Elements.FIRE;
        tag = ObjectType.ENEMY;

        width = objectSize.getObjectSize(tag);;
    }

    @Override
    public void update() {

    }
}
