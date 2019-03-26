package engine.entities;

import client.GameClient;
import engine.model.enums.Elements;
import engine.model.enums.ObjectType;
import javafx.geometry.Point2D;
import javafx.scene.transform.Rotate;


/**
 * A Character that is a player in the game
 */
public class Player extends Character {

    private static int nextId = 1;
    /**
     * The unique identify of each player in the game
     */
    private int id;

    /**
     * Constructor
     *
     * @param type the type of player it is Enemy or the client's player
     * @param id   The unique identify of this player
     */
    public Player(ObjectType type, int id) {
        super();
        this.id = id;
        location = new Point2D(0, 0);
        playerAngle = new Rotate(0);
        health = getMAX_HEALTH();
        movementSpeed = DEFAULT_MOVEMENT_SPEED;
        isShielded = false;
        //Default Fire
        currentElement = Elements.FIRE;
        tag = type;
        width = tag.getSize();
        height = width;
        actionHasSounded = false;
        lightAttackRange = width * 4;


        //Setup hashmap
        timeMap.put(CooldownItems.LIGHT, -10000L);
        timeMap.put(CooldownItems.HEAVY, -1000000L);
        timeMap.put(CooldownItems.CHANGESTATE, -10000L);
        timeMap.put(CooldownItems.DAMAGE, 0L);
        timeMap.put(CooldownItems.SPEED, 0L);

    }

    /**
     * Constructor
     *
     * @param type the type of player it is Enemy or the client's player
     */
    public Player(ObjectType type) {
        this(type, (type.equals(ObjectType.PLAYER) ? nextId++ : 0));
    }

    /**
     * Called every frame
     */
    @Override
    public void update() {

        if (health <= 0) {
            if (isAlive) {
                isAlive = false;
                deathTime = GameClient.timeElapsed;
            }
        } else {
            isAlive = true;
        }

        location = location.add(horizontalMove * GameClient.deltaTime, verticalMove * GameClient.deltaTime);
        horizontalMove = 0;
        verticalMove = 0;

        //decrease iframes every frame
        iframes--;


        //Changes movement speed back when duration has run out
        if (GameClient.timeElapsed - timeMap.get(CooldownItems.SPEED) >= CooldownValues.speedBoostDuration * 1000) {
            movementSpeed = DEFAULT_MOVEMENT_SPEED;
        }

        //Change damage multiplier when duration has run out
        if (GameClient.timeElapsed - timeMap.get(CooldownItems.DAMAGE) >= CooldownValues.damageBoostDur * 1000) {
            damageMultiplier = 1;
            damagePowerup = false;
        }


    }

    public void setLocation(double x, double y) {
        double X = this.location.getX();
        double Y = this.location.getY();

        this.location.add(x - X, y - Y);
    }

    /**
     * @return the player's id health and location
     */
    public String toString() {
        return "Player: " + this.id +
                "\nHealth: " + health +
                "\nx: " + location.getX() + " y: " + location.getY();
    }

    public int getId() {
        return id;
    }

    public void setId(int i) {
        this.id = i;
    }
}
