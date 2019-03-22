package engine.entities;

import client.GameClient;
import engine.model.enums.*;
import engine.physics.DeltaTime;
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

        location = location.add(horizontalMove * DeltaTime.deltaTime, verticalMove * DeltaTime.deltaTime);
        horizontalMove = 0;
        verticalMove = 0;

        //decrease iframes every frame
        iframes--;

    }

    public void setLocation(double x, double y) {
        this.location = new Point2D(x, y);
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

	public void setHealth(float playerCurrentHealth) {
		// TODO Auto-generated method stub
		this.health  = playerCurrentHealth;
    }

    public void setId(int i) {
        this.id = i;
    }
}
