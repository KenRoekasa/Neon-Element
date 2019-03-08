package engine.entities;

import client.GameClient;
import engine.enums.*;
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
        movementSpeed = DEFAULT_MOVEMENT_SPEED;
        isShielded = false;
        //Default Fire
        currentElement = Elements.FIRE;
        tag = type;
        width = tag.getSize();
        height = width;
        actionHasSounded = false;
        lightAttackRange = width * 4;


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
            if (isAlive) {
                isAlive = false;
            }
        } else {
            isAlive = true;
        }


        if(currentAction == Action.BLOCK) {
        		isShielded = true;

        }else {
        		isShielded = false;
        }
//        System.out.println(currentAction);

        location = location.add(horizontalMove * GameClient.deltaTime, verticalMove * GameClient.deltaTime);
        horizontalMove = 0;
        verticalMove = 0;

        //decrease iframes every frame
        iframes--;

    }

    public void setLocation(double x, double y) {
        this.location = new Point2D(x, y);
    }

    public void setId(int i) {
        this.id = i;
    }
    
    public void doAction(Action action) {
        switch(action) {
            case LIGHT:
                this.lightAttack();
                break;
            case CHARGE:
                this.chargeHeavyAttack();
                break;
            case BLOCK:
                this.shield();
                break;
            default:
                break;
        }
    }

    public String toString() {
        return "Player: " + this.id +
                "\nHealth: " + health;
    }


    public int getId() {
        return id;
    }
}
