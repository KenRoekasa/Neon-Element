package engine.entities;

import engine.enumSwitches.objectSize;
import engine.enums.Directions;
import engine.enums.Elements;
import engine.enums.ObjectType;
import javafx.geometry.Point2D;
import javafx.scene.transform.Rotate;


public class Player extends Character {

    private int id;


    public Player(ObjectType type) {
        location = new Point2D(0, 0);
        playerAngle = new Rotate(0);
        health = getMAX_HEALTH();
        characterDirection = Directions.UP;
        canUp = canDown = canLeft = canRight = canUpCart = canDownCart = canLeftCart = canRightCart = true;
        movementSpeed = DEFAULT_MOVEMENT_SPEED;
        isShielded = false;
        //Default Fire
        currentElement = Elements.FIRE;
        width = objectSize.getObjectSize(type);
        tag = type;


        for (int i = 0; i < timerArray.length; i++) {
            timerArray[i] = System.currentTimeMillis() - 10 * 1000;
        }
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
        if (health <= 0) {
        	if(isAlive) {
        		isAlive = false;
            	System.out.println("Player is Dead");
        	}
        } else {
            isAlive = true;
        }
    }

    public void setLocation(double x, double y) {
        double X = this.location.getX();
        double Y = this.location.getY();

        this.location.add(x - X, y - Y);
    }

    public void setID(int i) {
        this.id = i;
    }
}
