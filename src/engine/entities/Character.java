package engine.entities;

import client.GameClient;
import engine.model.AttackTimes;
import engine.model.enums.Action;
import engine.model.enums.Elements;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.util.Timer;
import java.util.TimerTask;

import static engine.entities.CooldownValues.*;

/**
 * All characters in this game
 */
public abstract class Character extends PhysicsObject {
    public static final float DEFAULT_MOVEMENT_SPEED = 0.35f;
    protected static final float MAX_HEALTH = 100;
    private final int heavyAttackRange = 500;
    protected float health;
    protected Elements currentElement;
    protected Rotate playerAngle;
    protected boolean isShielded;
    protected float movementSpeed;
    protected boolean isAlive = true;
    protected Action currentAction = Action.IDLE;
    /**
     * The time when you last died in milli
     */
    protected long deathTime = 0;

    /**
     * Is the character damaged boosted or not
     */
    protected boolean damagePowerup = false;
    /**
     * The number of frames the character is invulnerable for
     */
    protected int iframes = 0;
    /**
     * The amount of times the base damage is multiplied by
     * Used in damage boost buffs.
     */
    protected float damageMultiplier = 1;
    // The time the ability was last used System.time
    /**
     * An array of the last time a certain action was used. Using Static Id from CooldownValues
     */
    protected long[] timerArray = new long[10]; //TODO: Change the array length

    protected boolean actionHasSounded;

    /**
     * The players effective velocity (pixel per frame) in the vertical component
     */
    protected float verticalMove = 0;
    /**
     * The players effective velocity (pixel per frame) in the horizontal component
     */
    protected float horizontalMove = 0;
    /**
     * The last Character to inflict damage on this Character
     */
    protected Player lastAttacker = null;
    protected double lightAttackRange = 300;
    /**
     * The time in milliseconds at when the current action started
     */
    private long currentActionStart;

    /**
     * Move the Character up isometrically based off its movement speed
     */
    public void moveUp() {

        double yCheck = location.getY() - movementSpeed - width / 2f;
        double xCheck = location.getX() - movementSpeed - width / 2f;

        if (yCheck >= 0 && xCheck >= 0) {
            horizontalMove = -movementSpeed;
            verticalMove = -movementSpeed;
        }


    }

    /**
     * Move the Character down isometrically based off its movement speed
     *
     * @param boardWidth  the width of the map/board
     * @param boardHeight the height of the map/board
     */
    public void moveDown(double boardWidth, double boardHeight) {


        double yCheck = location.getY() + movementSpeed + width / 2f;
        double xCheck = location.getX() + movementSpeed + width / 2f;

        if (yCheck <= boardHeight && xCheck <= boardWidth) {
            horizontalMove = movementSpeed;
            verticalMove = movementSpeed;
        }
    }


    /**
     * Move the Character left isometrically based off its movement speed
     *
     * @param boardWidth the width of the map/board
     */
    public void moveLeft(double boardWidth) {

        double xCheck = location.getX() - movementSpeed - width / 2f;
        double yCheck = location.getY() + movementSpeed + width / 2f;

        if (xCheck >= 0 && yCheck <= boardWidth) {
            horizontalMove = -movementSpeed;
            verticalMove = movementSpeed;
        }
    }


    /**
     * Move the Character right isometrically based off its movement speed
     *
     * @param boardWidth  the width of the map/board
     * @param boardHeight the height of the map/board
     */
    public void moveRight(double boardWidth, double boardHeight) {

        //check within bounds

        double xCheck = location.getX() + movementSpeed + width / 2f;
        double yCheck = location.getY() - movementSpeed - width / 2f;


        if (xCheck <= boardWidth && yCheck >= 0) {

            horizontalMove = movementSpeed;
            verticalMove = -movementSpeed;
        }

    }

    /**
     * Move the Character right using cartesian coordinate system based off its movement speed
     */
    public void moveUpCartesian() {


        if ((location.getY() - movementSpeed - width / 2f) >= 0) {

            horizontalMove = 0;
            verticalMove = (float) (-Math.sqrt(2 * movementSpeed * movementSpeed));

        } else {
            location = new Point2D(location.getX(), 0 + width / 2f);
        }

    }

    /**
     * Move the Character down using cartesian coordinate system based off its movement speed
     *
     * @param boardHeight the height of the map/board
     */
    public void moveDownCartestian(double boardHeight) {


        if ((location.getY() + movementSpeed + width / 2f) <= boardHeight) {
            horizontalMove = 0;
            verticalMove = (float) (Math.sqrt(2 * movementSpeed * movementSpeed));
        } else {
            location = new Point2D(location.getX(), boardHeight - width / 2f);
        }


    }

    /**
     * Move the Character left using cartesian coordinate system based off its movement speed
     */
    public void moveLeftCartesian() {

        //check within bounds
        if ((location.getX() - movementSpeed - width / 2f) >= 0) {
            horizontalMove = (float) (-Math.sqrt(2 * movementSpeed * movementSpeed));
            verticalMove = 0;
        } else {
            location = new Point2D(0 + width / 2f, location.getY());
        }


    }

    /**
     * Move the Character right using cartesian coordinate system based off its movement speed
     *
     * @param boardWidth width of the map/board
     */
    public void moveRightCartesian(double boardWidth) {


        //check within bounds
        if ((location.getX() + movementSpeed + width / 2f) <= boardWidth) {
            horizontalMove = (float) (Math.sqrt(2 * movementSpeed * movementSpeed));
            verticalMove = 0;
        } else {
            location = new Point2D(boardWidth - width / 2f, location.getY());
        }

    }

    /**
     * Attack the character in front of this character
     */
    public void lightAttack() {
        if (checkCD(lightAttackID, lightAttackCD)) {
            if (currentAction == Action.IDLE) {
                actionHasSounded = false;
                currentAction = Action.LIGHT;
                currentActionStart = System.currentTimeMillis();
                long attackDuration = AttackTimes.getActionTime(currentAction);
                final long[] remainingAttackDuration = {currentActionStart + attackDuration - System.currentTimeMillis()};
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


    /**
     * Just simply removing health from the player but not from another player attack it
     *
     * @param damage the amount you want to remove the health from the player
     */
    public void removeHealth(float damage) {
        this.health -= damage;
    }

    /**
     * The Character is taking damage from another player
     *
     * @param damage       the amount the character is taking damge from
     * @param lastAttacker the person attacking the character
     */
    public void takeDamage(float damage, Player lastAttacker) {
        this.health -= damage;
        this.lastAttacker = lastAttacker;
        // iframes of the attacker increase
        // TODO: find perfect iframe value
        //Currently 65
        this.iframes = 15;
    }

    /**
     * Charge up a heavy attack for this player
     */
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


    /**
     * Heavy attack that deals AOE around this character
     */
    private void heavyAttack() {

        actionHasSounded = false;
        currentAction = Action.HEAVY;
        currentActionStart = System.currentTimeMillis();
        long attackDuration = AttackTimes.getActionTime(currentAction);
        final long[] remainingAttackDuration = {currentActionStart + attackDuration - System.nanoTime()/1000000};

        resetActionTimer(attackDuration, remainingAttackDuration);


    }

    /**
     * Creates a shield around this character to allow this character to take less damage
     */
    public void shield() {
        if (currentAction == Action.IDLE) {
            actionHasSounded = false;
            currentAction = Action.BLOCK;
            isShielded = true;
            movementSpeed = DEFAULT_MOVEMENT_SPEED / 2;

        }

    }

    /**
     * Stop the character from shielding
     */
    public void unShield() {
        if (currentAction == Action.BLOCK) {
            currentAction = Action.IDLE;
            movementSpeed = DEFAULT_MOVEMENT_SPEED;
            isShielded = false;
        }
    }

    /**
     * Change the character current element to fire
     */
    public void changeToFire() {
        if (currentAction == Action.IDLE) {
            if (checkCD(changeStateID, changeStateCD)) {
                currentElement = Elements.FIRE;
            }
        }
    }

    /**
     * Change the character current element to water
     */
    public void changeToWater() {
        if (currentAction == Action.IDLE) {
            if (checkCD(changeStateID, changeStateCD)) {
                currentElement = Elements.WATER;
            }
        }
    }

    /**
     * Change the character current element to earth
     */
    public void changeToEarth() {
        if (currentAction == Action.IDLE) {
            if (checkCD(changeStateID, changeStateCD)) {
                currentElement = Elements.EARTH;
            }
        }
    }

    /**
     * Change the character current element to air
     */
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

    /**
     * Causes a dead player to respawn. Its back to full health is now alive
     */
    public void respawn() {
        isAlive = true;
        health = MAX_HEALTH;
        iframes = 120;
    }


    /**
     * Adding health to the player
     *
     * @param amount the amount of health to add
     */
    public void addHealth(int amount) {

        health += amount;
        if (health > MAX_HEALTH) {
            health = MAX_HEALTH;
        }
    }

    /**
     * Increase movement speed
     */
    public void speedBoost() {
        System.out.println("SPEED");
        movementSpeed = DEFAULT_MOVEMENT_SPEED * 2;
        //Last time speed boost was activated
        timerArray[speedBoostID] = GameClient.timeElapsed;

    }

    /**
     * Doubles the players damage
     */
    public void damageBoost() {
        damageMultiplier = 2;
        damagePowerup = true;
        // if timer is not already running, run it
        //Last time damage boost was activated
        timerArray[damageBoostID] = GameClient.timeElapsed;


    }

    public boolean activeDamagePowerup() {
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
        return "ID: " + System.identityHashCode(this) +
                "\nHealth: " + health +
                "\nElement: " + currentElement.toString();
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    public Rectangle getAttackHitbox() {
        Rectangle hitbox = new Rectangle(location.getX(), location.getY() - lightAttackRange, width, lightAttackRange);
        Rotate rotate = new Rotate(playerAngle.getAngle(), location.getX() + (width / 2), location.getY() + (width / 2));
        hitbox.getTransforms().add(rotate);
        return hitbox;
    }

    public Circle getHeavyAttackHitbox() {
        return new Circle(location.getX() + width, location.getY() + width, heavyAttackRange);
    }


    public boolean hasActionSounded() {
        return actionHasSounded;
    }

    public void setActionHasSounded(boolean actionHasSounded) {
        this.actionHasSounded = actionHasSounded;
    }

    /**
     * Check if the action is off cooldown.
     *
     * @param id       the id of the cooldown based of the cooldown values static class
     * @param cooldown the duration of the cooldown
     * @returnTrue if the action is off cooldown; false otherwise
     */
    private boolean checkCD(int id, float cooldown) {
        if (GameClient.timeElapsed - timerArray[id] >= (long) (cooldown * 1000)) {
            timerArray[id] = GameClient.timeElapsed;
            return true;
        }
        return false;
    }

    public int getIframes() {
        return iframes;
    }

    public void setHorizontalMove(float horizontalMove) {
        this.horizontalMove = horizontalMove;
    }

    public void setVerticalMove(float verticalMove) {
        this.verticalMove = verticalMove;
    }

    public Player getLastAttacker() {
        return lastAttacker;
    }

    public float getVerticalMove() {
        return verticalMove;
    }

    public float getHorizontalMove() {
        return horizontalMove;
    }

    public long getDeathTime() {
        return deathTime;
    }
}
