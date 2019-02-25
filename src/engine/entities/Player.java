package engine.entities;

import engine.enums.Directions;
import engine.enums.Elements;
import engine.enums.ObjectType;
import javafx.geometry.Point2D;
import javafx.scene.transform.Rotate;


public class Player extends Character {

    private int id;

    private static int nextId = 1;

    public Player(ObjectType type, int id) {
        super();
        this.id = id;
        location = Point2D.ZERO;
        playerAngle = new Rotate(0);
        health = getMAX_HEALTH();
        characterDirection = Directions.UP;
        canUp = canDown = canLeft = canRight = canUpCart = canDownCart = canLeftCart = canRightCart = true;
        movementSpeed = DEFAULT_MOVEMENT_SPEED;
        isShielded = false;
        //Default Fire
        currentElement = Elements.FIRE;
        tag = type;
        width = tag.getSize();
        actionHasSounded = false;


        for (int i = 0; i < timerArray.length; i++) {
            timerArray[i] = System.currentTimeMillis() - 10 * 1000;
        }
    }

    public Player(ObjectType type) {
        this(type, (type.equals(ObjectType.PLAYER) ? nextId++ : 0));
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
        this.location = new Point2D(x, y);
    }

    public void setId(int i) {
        this.id = i;
    }

    public String toString(){
        String s = "Player: " + this.id +
                "\nHealth: " + health;
        return s;
    }


    public int getId() {
        return id;
    }
}
