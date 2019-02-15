package entities;

import calculations.AttackTimes;
import enums.Action;
import enums.Directions;
import enums.Elements;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.util.Timer;
import java.util.TimerTask;

import static entities.CooldownValues.*;

public abstract class Character extends PhysicsObject {
    protected int id;
    
    protected float health;
    protected Elements currentElement;
    protected Rotate playerAngle;


    protected Directions characterDirection;
    protected boolean isShielded;
    protected int movementSpeed;
    protected final float MAX_HEALTH = 100;
    protected boolean isAlive = true;
    protected Action currentAction = Action.IDLE;

    public boolean canUp, canDown, canLeft, canRight, canUpCart, canDownCart, canLeftCart, canRightCart;


    // The time the ability was last used System.time

    protected long[] timerArray = new long[10];

    //TODO: Change the access modifier
    public boolean isColliding;
    private Timer timer = new Timer();


    private long currentActionStart;
    
    private static int nextId = 1;
    

    Character() {
        this.id = Character.nextId++;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public void moveUp() {
        characterDirection = Directions.UP;
        if (canUp) {
            double yCheck = location.getY() - movementSpeed - width / 2f;
            double xCheck = location.getX() - movementSpeed - width / 2f;

            if (yCheck >= 0 && xCheck >= 0 && !isColliding) {
                location = location.add(-movementSpeed, -movementSpeed);
            }
        }


    }

    public void moveDown(double boardWidth, double boardHeight) {
        characterDirection = Directions.DOWN;
        if (canDown) {

            double yCheck = location.getY() + movementSpeed + width / 2f;
            double xCheck = location.getX() + movementSpeed + width / 2f;

            if (yCheck <= boardHeight && xCheck <= boardWidth && !isColliding) {
                location = location.add(movementSpeed, movementSpeed);
            }
        }
    }

    public void moveLeft(double boardWidth) {
        characterDirection = Directions.LEFT;
        if (canLeft) {

            double xCheck = location.getX() - movementSpeed - width / 2f;
            double yCheck = location.getY() + movementSpeed + width / 2f;

            if (xCheck >= 0 && yCheck <= boardWidth && !isColliding) {
                location = location.add(-movementSpeed, movementSpeed);
            }
        }
    }

    public void moveRight(double boardWidth, double boardHeight) {
        characterDirection = Directions.RIGHT;
        if (canRight) {
            //check within bounds

            double xCheck = location.getX() + movementSpeed + width / 2f;
            double yCheck = location.getY() - movementSpeed - width / 2f;


            if (xCheck <= boardWidth && yCheck >= 0 && !isColliding) {
                location = location.add(movementSpeed, -movementSpeed);
            }
        }
    }

    public void moveUpCartesian() {
        characterDirection = Directions.UPCART;
        if (canUpCart) {

            if ((location.getY() - movementSpeed - width / 2f) >= 0) {
                location = location.add(0, -(movementSpeed * 2));
            } else {
                location = new Point2D(location.getX(), 0 + width / 2f);
            }
        }
    }

    public void moveDownCartestian(double boardHeight) {
        characterDirection = Directions.DOWNCART;
        if (canDownCart) {

            if ((location.getY() + movementSpeed + width / 2f) <= boardHeight) {
                location = location.add(0, (movementSpeed * 2));
            } else {
                location = new Point2D(location.getX(), boardHeight - width / 2f);
            }
        }

    }

    public void moveLeftCartesian() {
        characterDirection = Directions.LEFTCART;
        if (canLeftCart) {

            //check within bounds
            if ((location.getX() - movementSpeed - width / 2f) >= 0) {
                location = location.add(-(movementSpeed * 2), 0);
            } else {
                location = new Point2D(0 + width / 2f, location.getY());
            }
        }

    }

    public void moveRightCartesian(double boardWidth) {
        characterDirection = Directions.RIGHTCART;
        if (canRightCart) {

            //check within bounds
            if ((location.getX() + movementSpeed + width / 2f) <= boardWidth) {
                location = location.add((movementSpeed * 2), 0);
            } else {
                location = new Point2D(boardWidth - width / 2f, location.getY());
            }
        }
    }

    public void lightAttack() {

        if (currentAction == Action.IDLE) {

            currentAction = Action.LIGHT;
            currentActionStart = System.currentTimeMillis();
            long attackDuration = AttackTimes.getActionTime(currentAction);
            final long[] remainingAttackDuration = {currentActionStart + attackDuration - System.currentTimeMillis()};


            int damage = 3;


            resetActionTimer(attackDuration, remainingAttackDuration);
        }


    }

    private void resetActionTimer(long attackDuration, long[] remainingAttackDuration) {
        (new Thread(() -> {
            while (remainingAttackDuration[0] > 0) {
                remainingAttackDuration[0] = currentActionStart + attackDuration - System.currentTimeMillis();
            }
            currentAction = Action.IDLE;
        })).start();

    }


    public void removeHealth(float damage) {
        this.health -= damage;
    }

    public void chargeHeavyAttack() {
        // TODO handle charging

        if (currentAction == Action.IDLE) {

            currentAction = Action.CHARGE;
            currentActionStart = System.currentTimeMillis();

            (new Thread(() -> {

                try {
                    Thread.sleep(AttackTimes.getActionTime(currentAction));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                heavyAttack();
            })).start();
        }
    }

    public void heavyAttack() {
        currentAction = Action.HEAVY;
        currentActionStart = System.currentTimeMillis();
        long attackDuration = AttackTimes.getActionTime(currentAction);
        final long[] remainingAttackDuration = {currentActionStart + attackDuration - System.currentTimeMillis()};
        //TODO: DO SOMETHING

        resetActionTimer(attackDuration, remainingAttackDuration);


    }

    public void shield() {
        if (currentAction == Action.IDLE) {
            currentAction = Action.BLOCK;
            isShielded = true;


//            long nextAvailableTime = (long) (timerArray[shieldID] + (shieldCD * 1000));
//            if (System.currentTimeMillis() > nextAvailableTime) {
//                //need code to unshield after a certain duration
//
//                // set last time spell was used
//                timerArray[shieldID] = System.currentTimeMillis();
//            }
        }
    }

    public void unShield() {
        if (currentAction == Action.BLOCK) {
            currentAction = Action.IDLE;
            isShielded = false;
        }
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
        this.location = location;
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

    public void isColliding(PhysicsObject e) {
        isColliding = true;
        //                double xDiff = (getBounds().getBoundsInParent().getMinX() - e.getBounds().getBoundsInParent().getMinX());
        //                double yDiff = (getBounds().getBoundsInParent().getMinY() - e.getBounds().getBoundsInParent().getMinY());
        double xDiff = location.getX() - e.getLocation().getX();
        double yDiff = location.getY() - e.getLocation().getY();
        switch (characterDirection) {
            case UP:
                // if on the right side of the box
                // if player min x > e player max x
                if (getBounds().getBoundsInParent().getMinX() >= e.getBounds().getBoundsInParent().getMaxX()) {
                    location = location.add((width - xDiff) + movementSpeed, (width - xDiff) + movementSpeed);
                    break;
                } else {
                    location = location.add((width - yDiff) + movementSpeed, (width - yDiff) + movementSpeed);
                    break;
                }
                //                location = location.add((width - yDiff) + movementSpeed, (width - xDiff)+movementSpeed);

            case DOWN:
                location = location.add(-movementSpeed, -movementSpeed);
                break;
            case LEFT:
                location = location.add(movementSpeed, -movementSpeed);
                break;

            case RIGHT:
                location = location.add(-movementSpeed, movementSpeed);
                break;
            case UPCART:
                //                location = new Point2D(location.getX(),location.getY()+yDiff);
                location = location.add(0, (movementSpeed * 2));
                break;
            case DOWNCART:
                location = location.add(0, -(movementSpeed * 2));
                break;
            case LEFTCART:
                location = location.add((movementSpeed * 2), 0);
                break;
            case RIGHTCART:
                location = location.add(-(movementSpeed * 2), 0);
                break;
            default:
                break;

        }

    }


    // adds Health to the player
    public void addHealth(int amount) {
        health += amount;
        if (health > 100) {
            health = 100;
        }
    }

    // Increase movement speed
    public void speedBoost() {
        movementSpeed = 8;
        // if timer is not already running, run it
        if (timerArray[speedBoostID] > 0) {
            timerArray[speedBoostID] = 0;
            //counts for 4 seconds then back to default movement speed
            (new Timer()).scheduleAtFixedRate(new TimerTask() {

                public void run() {
                    if (timerArray[speedBoostID] == speedBoostDuration) {
                        movementSpeed = 5;
                        timer.cancel();
                    }
                    timerArray[speedBoostDuration]++;
                }
            }, 0, 1000);
        } else {
            timerArray[speedBoostID] = 0;
        }
    }


    // Doubles the players damage
    public void damageBoost() {


    }

    public Action getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(Action currentAction) {
        this.currentAction = currentAction;
    }

    public long getCurrentActionStart() {
        return currentActionStart;
    }

    public Rectangle getAttackHitbox() {
        Rectangle hitbox = new Rectangle(location.getX(), location.getY() - width, width, width);
        Rotate rotate = new Rotate(playerAngle.getAngle(), location.getX() + (width / 2), location.getY() + (width / 2));
        hitbox.getTransforms().add(rotate);
        return hitbox;
    }

    public Circle getHeavyAttackHitbox() {
        return new Circle(location.getX() + width, location.getY() + width, 200);
    }

    public Directions getCharacterDirection() {
        return characterDirection;
    }

}
