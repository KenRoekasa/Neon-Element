package Entities;

import Enums.Directions;
import Enums.Elements;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.util.Timer;
import java.util.TimerTask;

public abstract class Character extends PhysicsObject {
    protected float health;
    protected Elements currentElement;
    protected Rotate playerAngle;
    protected Directions characterDirection;
    protected boolean isShielded;
    protected int movementSpeed;
    protected final float MAX_HEALTH = 100;
    protected boolean isAlive = true;

    //TODO: Change the access modifier
    public boolean isColliding;

    private Timer timer = new Timer();
    private Rectangle attackHitbox = new Rectangle(width, width);

    public void moveUp() {
        characterDirection = Directions.UP;

        double yCheck = location.getY() - movementSpeed - width / 2f;
        double xCheck = location.getX() - movementSpeed - width / 2f;

        if (yCheck >= 0 && xCheck >= 0) {
            location = location.add(-movementSpeed, -movementSpeed);
        }


    }

    public void moveDown(double boardWidth, double boardHeight) {
        characterDirection = Directions.DOWN;

        double yCheck = location.getY() + movementSpeed + width / 2f;
        double xCheck = location.getX() + movementSpeed + width / 2f;

        if (yCheck <= boardHeight && xCheck <= boardWidth) {
            location = location.add(movementSpeed, movementSpeed);
        }
    }

    public void moveLeft(double boardWidth) {
        characterDirection = Directions.LEFT;

        double xCheck = location.getX() - movementSpeed - width / 2f;
        double yCheck = location.getY() + movementSpeed + width / 2f;

        if (xCheck >= 0 && yCheck <= boardWidth) {
            location = location.add(-movementSpeed, movementSpeed);
        }
    }

    public void moveRight(double boardWidth, double boardHeight) {
        characterDirection = Directions.RIGHT;
        //check within bounds

        double xCheck = location.getX() + movementSpeed + width / 2f;
        double yCheck = location.getY() - movementSpeed - width / 2f;


        if (xCheck <= boardWidth && yCheck >= 0) {
            location = location.add(movementSpeed, -movementSpeed);
        }
    }

    public void moveUpCartesian() {
        characterDirection = Directions.UP;

        if ((location.getY() - movementSpeed - width / 2f) >= 0) {
            location = location.add(0, -(movementSpeed * 2));
        } else {
            location = new Point2D(location.getX(), 0 + width / 2f);
        }
    }

    public void moveDownCartestian(double boardHeight) {
        characterDirection = Directions.DOWN;

        if ((location.getY() + movementSpeed + width / 2f) <= boardHeight) {
            location = location.add(0, (movementSpeed * 2));
        } else {
            location = new Point2D(location.getX(), boardHeight - width / 2f);
        }

    }

    public void moveLeftCartesian() {
        characterDirection = Directions.LEFT;

        //check within bounds
        if ((location.getX() - movementSpeed - width / 2f) >= 0) {
            location = location.add(-(movementSpeed * 2), 0);
        } else {
            location = new Point2D(0 + width / 2f, location.getY());
        }

    }

    public void moveRightCartesian(double boardWidth) {
        characterDirection = Directions.RIGHT;

        //check within bounds
        if ((location.getX() + movementSpeed + width / 2f) <= boardWidth) {
            location = location.add((movementSpeed * 2), 0);
        } else {
            location = new Point2D(boardWidth - width / 2f, location.getY());
        }
    }

    public void lightAttack() {
        int damage = 3;
        //set attack hit box in front of the user
        //TODO: Change hitbox location based on rotation too, so the hitbox is in front of the player
        switch (characterDirection) {
            case UP:
                attackHitbox.setX(location.getX() - width);
                attackHitbox.setY(location.getY() - width);
                break;
            case DOWN:
                attackHitbox.setX(location.getX() + width);
                attackHitbox.setY(location.getY() + width);
                break;
            case LEFT:
                attackHitbox.setX(location.getX() - width);
                attackHitbox.setY(location.getY() + width);
                break;
            case RIGHT:
                attackHitbox.setX(location.getX() + width);
                attackHitbox.setY(location.getY() - width);
                break;
        }

        //temp array for the other players
        Character otherCharacters[] = new Character[4];


        //If another Character is in the Hitbox calculate the damage they take
        // How is damaged dealt throught the victim or the attacker or server
        for (Character p : otherCharacters) {
            if (attackHitbox.intersects(p.getBounds().getBoundsInParent())) {
                //TODO: What happens when you hit another Character
                //sends to server
            }
        }


    }

    public void removeHealth(float damage) {
        this.health -= damage;
    }

    public void heavyAttack() {


    }

    public void shield() {
        //need code to unshield after a certain duration
        isShielded = true;

        //counts for 10 seconds then unshield
        final int[] timeCtr = {0};
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                if (timeCtr[0] == 10) {
                    isShielded = false;
                    timer.cancel();
                }
                timeCtr[0]++;
            }
        }, 0, 1000);

    }

    public void changeToFire() {
        currentElement = Elements.FIRE;
    }

    public void changeToWater() {
        currentElement = Elements.WATER;
    }

    public void changeToEarth() {
        currentElement = Elements.EARTH;
    }

    public void changeToAir() {
        currentElement = Elements.AIR;
    }

    public Rotate getPlayerAngle() {
        return playerAngle;
    }

    public void setPlayerAngle(Rotate playerAngle) {
        this.playerAngle = playerAngle;
    }


    public void setLocation(Point2D location) {
        this.location = location.add(width / 2f, width / 2f);
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public float getHealth() {
        return health;
    }

    public Elements getCurrentElement() {
        return currentElement;
    }


    public boolean isShielded() {
        return isShielded;
    }

    public float getMAX_HEALTH() {
        return MAX_HEALTH;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void isColliding(double xDiff, double yDiff) {
        location = location.add(xDiff,yDiff);

    }
}
