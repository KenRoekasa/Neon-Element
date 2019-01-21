package Entities;

import Enums.PlayerStates;
import javafx.geometry.Point2D;


public class Player {
    private Point2D location = new Point2D(0, 0);
    private final int WIDTH = 20;
    private int movementSpeed = 10;
    //Can be a float
    private float health = 100;
    private final float MAX_HEALTH = 100;
    private PlayerStates state;

    //The countdown of the changestate cooldown
    private float changeStateCurrentCD;
    //The number of seconds for change state to go off cooldown
    private final float CHANGESTATECD = 1.2f;


    private void lightAttack() {
        switch (state) {
            case AIR:
                //attack using air
                break;
            case FIRE:
                //attack using fire
                break;
            case EARTH:
                //attack using earth
                break;
            case WATER:
                //attack using water
                break;
            default:
                //What should happen?
                break;
        }

    }

    private void heavyAttack() {
        switch (state) {
            case AIR:
                //attack using air
                break;
            case FIRE:
                //attack using fire
                break;
            case EARTH:
                //attack using earth
                break;
            case WATER:
                //attack using water
                break;
            default:
                //What should happen?
                break;
        }

    }

    private void shield() {
        switch (state) {
            case AIR:
                //attack using air
                break;
            case FIRE:
                //attack using fire
                break;
            case EARTH:
                //attack using earth
                break;
            case WATER:
                //attack using water
                break;
            default:
                //What should happen?
                break;
        }

    }

    public void changeToFire() {
        state = PlayerStates.FIRE;
        changeStateCurrentCD = CHANGESTATECD;
    }

    public void changeToWater() {
        state = PlayerStates.WATER;
        changeStateCurrentCD = CHANGESTATECD;
    }

    public void changeToEarth() {
        state = PlayerStates.EARTH;
        changeStateCurrentCD = CHANGESTATECD;
    }


    //teleport a certain amount in front the character or we could have a speed boost
    private void dash() {


    }


    public void moveUp() {
        if ((location.getY() - movementSpeed - WIDTH / 2f) >= 0) {
            location = location.add(-movementSpeed, -movementSpeed);
        } else {
            location = new Point2D(location.getX(), 0 + WIDTH / 2f);
        }
    }

    public void moveDown(double boardHeight) {

        if ((location.getY() + movementSpeed + WIDTH / 2f) <= boardHeight) {
            location = location.add(movementSpeed, movementSpeed);
        } else {
            location = new Point2D(location.getX(), boardHeight - WIDTH / 2f);
        }

    }

    public void moveLeft() {
        //check within bounds
        if ((location.getX() - movementSpeed - WIDTH / 2f) >= 0) {
            location = location.add(-movementSpeed, movementSpeed);
        } else {
            location = new Point2D(0 + WIDTH / 2f, location.getY());
        }

    }

    public void moveRight(double boardWidth) {
        //check within bounds
        if ((location.getX() + movementSpeed + WIDTH / 2f) <= boardWidth) {
            location = location.add(movementSpeed, -movementSpeed);
        } else {
            location = new Point2D(boardWidth - WIDTH / 2f, location.getY());
        }
    }


    public Point2D getLocation() {
        return location;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public float getHealth() {
        return health;
    }

    public float getMAX_HEALTH() {
        return MAX_HEALTH;
    }

    public void setLocation(Point2D location) {
        this.location = location.add(WIDTH / 2f, WIDTH / 2f);
    }

}
