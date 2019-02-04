package entities;

import enums.Elements;
import javafx.geometry.Point2D;
import javafx.scene.transform.Rotate;

public class Enemy extends Character {

    public Enemy() {
        location = new Point2D(10, 100);
        playerAngle = new Rotate(0);
        health = 100;
        movementSpeed = 2;
        isShielded = false;
        //Default Fire
        currentElement = Elements.FIRE;
        width = 20;
    }

    @Override
    public void update() {

    }
}
