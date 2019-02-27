package engine.entities;

import client.audiomanager.AudioManager;
import engine.calculations.AttackTimes;
import engine.enums.Action;
import engine.enums.Directions;
import engine.enums.Elements;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static engine.entities.CooldownValues.*;

public abstract class Character extends PhysicsObject {
    public static final float DEFAULT_MOVEMENT_SPEED = 0.35f;
    protected static final float MAX_HEALTH = 100;
    public boolean canUp, canDown, canLeft, canRight, canUpCart, canDownCart, canLeftCart, canRightCart;
    protected float health;
    protected Elements currentElement;
    protected Rotate playerAngle;
    protected Directions characterDirection;
    protected boolean isShielded;
    protected float movementSpeed;
    protected boolean isAlive = true;
    protected Action currentAction = Action.IDLE;
    protected boolean damagePowerup=false;
    // Used in damage boost buffs
    protected float damageMultiplier = 1;
    // The time the ability was last used System.time
    protected long[] timerArray = new long[10]; //TODO: Change the array length

    protected boolean actionHasSounded;




    protected float verticalMove = 0;
    protected float horizontalMove = 0;
    protected Player lastAttacker;
    private long currentActionStart;

    public Player getLastAttacker() {
        return lastAttacker;
    }

    public void moveUp() {
        characterDirection = Directions.UP;
        if (canUp) {
            double yCheck = location.getY() - movementSpeed  - width / 2f;
            double xCheck = location.getX() - movementSpeed  - width / 2f;

            if (yCheck >= 0 && xCheck >= 0) {
                horizontalMove = -movementSpeed ;
                verticalMove = -movementSpeed ;
            }
        }


    }

    public void moveDown(double boardWidth, double boardHeight) {
        characterDirection = Directions.DOWN;
        if (canDown) {

            double yCheck = location.getY() + movementSpeed  + width / 2f;
            double xCheck = location.getX() + movementSpeed  + width / 2f;

            if (yCheck <= boardHeight && xCheck <= boardWidth) {
                horizontalMove = movementSpeed ;
                verticalMove = movementSpeed ;
            }
        }
    }

    public void moveLeft(double boardWidth) {
        characterDirection = Directions.LEFT;
        if (canLeft) {
            double xCheck = location.getX() - movementSpeed  - width / 2f;
            double yCheck = location.getY() + movementSpeed  + width / 2f;

            if (xCheck >= 0 && yCheck <= boardWidth) {
                horizontalMove = -movementSpeed ;
                verticalMove = movementSpeed ;
            }
        }
    }

    public void moveRight(double boardWidth, double boardHeight) {
        characterDirection = Directions.RIGHT;
        if (canRight) {
            //check within bounds

            double xCheck = location.getX() + movementSpeed  + width / 2f;
            double yCheck = location.getY() - movementSpeed  - width / 2f;


            if (xCheck <= boardWidth && yCheck >= 0) {

                horizontalMove = movementSpeed ;
                verticalMove = -movementSpeed ;
            }
        }
    }

    public void moveUpCartesian() {
        characterDirection = Directions.UPCART;
        if (canUpCart) {

            if ((location.getY() - movementSpeed  - width / 2f) >= 0) {

                horizontalMove = 0;
                verticalMove = (float) (-Math.sqrt(2 * movementSpeed * movementSpeed) );

            } else {
                location = new Point2D(location.getX(), 0 + width / 2f);
            }
        }
    }

    public void moveDownCartestian(double boardHeight) {
        characterDirection = Directions.DOWNCART;
        if (canDownCart) {

            if ((location.getY() + movementSpeed  + width / 2f) <= boardHeight) {
                horizontalMove = 0;
                verticalMove = (float) (Math.sqrt(2 * movementSpeed * movementSpeed) );
            } else {
                location = new Point2D(location.getX(), boardHeight - width / 2f);
            }
        }

    }

    public void moveLeftCartesian() {
        characterDirection = Directions.LEFTCART;
        if (canLeftCart) {
            //check within bounds
            if ((location.getX() - movementSpeed  - width / 2f) >= 0) {
                horizontalMove = (float) (-Math.sqrt(2 * movementSpeed * movementSpeed) );
                verticalMove = 0;
            } else {
                location = new Point2D(0 + width / 2f, location.getY());
            }
        }

    }

    public void moveRightCartesian(double boardWidth) {
        characterDirection = Directions.RIGHTCART;
        if (canRightCart) {

            //check within bounds
            if ((location.getX() + movementSpeed  + width / 2f) <= boardWidth) {
                horizontalMove = (float) (Math.sqrt(2 * movementSpeed * movementSpeed) );
                verticalMove = 0;
            } else {
                location = new Point2D(boardWidth - width / 2f, location.getY());
            }
        }
    }

    public void lightAttack() {
        if (checkCD(lightAttackID, lightAttackCD)) {
            if (currentAction == Action.IDLE) {
                actionHasSounded = false;

                currentAction = Action.LIGHT;
                currentActionStart = System.currentTimeMillis();
                long attackDuration = AttackTimes.getActionTime(currentAction);
                final long[] remainingAttackDuration = {currentActionStart + attackDuration - System.currentTimeMillis()};
                int damage = 3;
                resetActionTimer(attackDuration, remainingAttackDuration);
            }
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
        //System.out.println("player health "+getHealth());
    }

    public void removeHealth(float damage, Player lastAttacker) {
        this.health -= damage;
        this.lastAttacker = lastAttacker;
        //System.out.println("player health "+getHealth());
    }
    public void chargeHeavyAttack() {
        // TODO handle charging
        if (checkCD(heavyAttackID, heavyAttackCD)) {
            if (currentAction == Action.IDLE) {
                actionHasSounded = false;
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
    }


    private void heavyAttack() {

        actionHasSounded = false;
        currentAction = Action.HEAVY;
        currentActionStart = System.currentTimeMillis();
        long attackDuration = AttackTimes.getActionTime(currentAction);
        final long[] remainingAttackDuration = {currentActionStart + attackDuration - System.currentTimeMillis()};

        resetActionTimer(attackDuration, remainingAttackDuration);


    }

    public void shield() {
        if (currentAction == Action.IDLE) {
            actionHasSounded = false;
            currentAction = Action.BLOCK;
            isShielded = true;
            movementSpeed = DEFAULT_MOVEMENT_SPEED / 2;

        }
    }

    public void unShield() {
        if (currentAction == Action.BLOCK) {
            currentAction = Action.IDLE;
            movementSpeed = DEFAULT_MOVEMENT_SPEED;
            isShielded = false;
        }
    }

    public void changeToFire() {
        if (currentAction == Action.IDLE) {
            if (checkCD(changeStateID, changeStateCD)) {
                currentElement = Elements.FIRE;
            }
        }
    }

    public void changeToWater() {
        if (currentAction == Action.IDLE) {
            if (checkCD(changeStateID, changeStateCD)) {
                currentElement = Elements.WATER;
            }
        }
    }

    public void changeToEarth() {
        if (currentAction == Action.IDLE) {
            if (checkCD(changeStateID, changeStateCD)) {
                currentElement = Elements.EARTH;
            }
        }
    }

    public void changeToAir() {
        if (currentAction == Action.IDLE) {
            if (checkCD(changeStateID, changeStateCD)) {
                currentElement = Elements.AIR;
            }
        }
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

    public float getMovementSpeed() {
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

    public void respawn() {
        isAlive = true;
        health = MAX_HEALTH;
    }

    // adds Health to the player

    public void addHealth(int amount) {

        health += amount;
        if (health > MAX_HEALTH) {
            health = MAX_HEALTH;
        }
    }

    // Increase movement speed

    public void speedBoost() {
        Timer timer = new Timer();

        movementSpeed = DEFAULT_MOVEMENT_SPEED * 2;
        // if timer is not already running, run it
        if (timerArray[speedBoostID] > 0) {
            timerArray[speedBoostID] = 0;
            //counts for 4 seconds then back to default movement speed
            (new Timer()).scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    if (timerArray[speedBoostID] == speedBoostDuration) {
                        movementSpeed = DEFAULT_MOVEMENT_SPEED;
                        System.out.println("speed boost has ended");
                        timer.cancel();
                    }
                    timerArray[speedBoostID]++;
                }
            }, 0, 1000);
        } else {
            timerArray[speedBoostID] = 0;
        }
    }

    // Doubles the players damage

    public void damageBoost() {
        Timer timer = new Timer();
        damageMultiplier = 2;
        damagePowerup = true;
        // if timer is not already running, run it
        if (timerArray[damageBoostID] > 0) {
            timerArray[damageBoostID] = 0;
            //counts for 4 seconds then back to default movement speed
            (new Timer()).scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    if (timerArray[damageBoostID] == damageBoostDur) {
                        damageMultiplier = 1;
                        damagePowerup = false;
                        timer.cancel();
                    }
                    timerArray[damageBoostID]++;
                }
            }, 0, 1000);
        } else {
        	damagePowerup = false;
            timerArray[damageBoostID] = 0;
        }

    }

    public boolean activeDamagePowerup () {
    	return damagePowerup;
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

    public String toString() {
        String s = "ID: " + System.identityHashCode(this) +
                "\nHealth: " + health +
                "\nElement: " + currentElement.toString();
        return s;
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }
    public Rectangle getAttackHitbox() {
        Rectangle hitbox = new Rectangle(location.getX(), location.getY() - width, width, width);
        Rotate rotate = new Rotate(playerAngle.getAngle(), location.getX() + (width / 2), location.getY() + (width / 2));
        hitbox.getTransforms().add(rotate);
        return hitbox;
    }

    public Circle getHeavyAttackHitbox() {
        return new Circle(location.getX() + width, location.getY() + width, 300);
    }

    public Directions getCharacterDirection() {
        return characterDirection;
    }


    public boolean hasActionSounded() {
        return actionHasSounded;
    }

    public void setActionHasSounded(boolean actionHasSounded) {
        this.actionHasSounded = actionHasSounded;
    }

    //check if the action is off cooldown
    private boolean checkCD(int id, float cooldown) {
        // get the time it was last used and add the cooldown
        long nextAvailableTime = (timerArray[id] + (long) (cooldown * 1000));
        //check if the time calculated has passed
        if (System.currentTimeMillis() > nextAvailableTime) {
            timerArray[id] = System.currentTimeMillis();
            return true;
        }
        return false;
    }


    public void delay(int time) {
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
