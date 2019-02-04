package entities;

import enums.Elements;
import javafx.geometry.Point2D;
import javafx.scene.transform.Rotate;


public class Player extends Character {

    private int id;

    //COOLDOWNS
    //The number of seconds for change state to go off cooldown
    private final float CHANGE_STATE_CD = 1.2f;
    // The countdown of the changestate cooldown
    private float changeStateCurrentCD;


    public Player() {
        location = new Point2D(0, 0);
        playerAngle = new Rotate(0);
        health = 100;
        movementSpeed = 2;
        isShielded = false;
        //Default Fire
        currentElement = Elements.FIRE;
        width = 20;
    }

    public Player(int id) {
        super();
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public void update() { // Called every game tick, put location updates server sending etc... here
        if (health < 0) {
            isAlive = false;
        } else {
            isAlive = true;
        }

    }

    public void setLocation(double x, double y) {
        double X = this.location.getX();
        double Y = this.location.getY();

        this.location.add(x-X, y-Y);
    }
}
