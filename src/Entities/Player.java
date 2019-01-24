package Entities;

import Calculations.DamageCalculation;
import Enums.PlayerStates;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class Player {

    private final int MAX_NUM_BUFFS = 4;
    private Point2D location = new Point2D(0, 0);
    private Rotate playerAngle = new Rotate(0);
    private final int WIDTH = 20;


    private float health = 100;
    private final float MAX_HEALTH = 100;
    private int movementSpeed = 10;
    private boolean isShielded = false;


    //Default Fire
    private PlayerStates state = PlayerStates.FIRE;

    private Rectangle attackHitbox = new Rectangle(WIDTH, WIDTH);


    // An array of the power up that the player has picked up
    private BlockingQueue<PowerUp> buffs = new ArrayBlockingQueue<PowerUp>(MAX_NUM_BUFFS);

    //COOLDOWNS
    //The number of seconds for change state to go off cooldown
    private final float CHANGE_STATE_CD = 1.2f;
    // The countdown of the changestate cooldown
    private float changeStateCurrentCD;


    //TODO: this should moved to the server
    private DamageCalculation dmgCal = new DamageCalculation();

    public Player(){




        // activate the powerUps that the players have picked up
        Thread powerUpLoop = new Thread(new Runnable() {
            @Override
            public void run() {

                //Todo: Take the powerup from the queue and activate the powerup to become a buff
            }
        });

    }


    private void lightAttack() {
        int damage = 3;

        //set attack hit box in front of the user
        //TODO: Change hitbox location based on rotation too, so the hitbox is in front of the player
        attackHitbox.setX(location.getX() + WIDTH);
        attackHitbox.setY(location.getX() + WIDTH);


        //temp array for the other players
        Player otherPlayer[] = new Player[4];


        //If another player is in the Hitbox calculate the damage they take
        // How is damaged dealt throught the victim or the attacker or server
        for (Player p : otherPlayer) {
            if (attackHitbox.intersects(p.getHitBox().getBoundsInParent())) {
                //The damage with damage multiplier based on state and calculate mitigation based on if the victim has a shield or not
                float damageToBeTaken = dmgCal.calculateDamage(damage, this, p) * dmgCal.calculateMitgation(this, p);
                //set request to server
                p.removeHealth(damageToBeTaken);
            }
        }





    }

    private void removeHealth(float damage) {
        this.health -= damage;
    }

    private void heavyAttack() {


    }

    private void shield() {

        //need code to unshield after a certain duration
        isShielded = true;


    }

    public void changeToFire() {
        state = PlayerStates.FIRE;
        changeStateCurrentCD = CHANGE_STATE_CD;
    }

    public void changeToWater() {
        state = PlayerStates.WATER;
        changeStateCurrentCD = CHANGE_STATE_CD;
    }

    public void changeToEarth() {
        state = PlayerStates.EARTH;
        changeStateCurrentCD = CHANGE_STATE_CD;
    }


  // Todo: Rework how powerUps interact with the player
    public void addBuff(PowerUp powerUp) {
        buffs.add(powerUp);
    }


    //teleport a certain amount in front the character or we could have a speed boost
    private void dash() {

    }

    public void moveUp() {

        double yCheck = location.getY() - movementSpeed - WIDTH / 2f;
        double xCheck = location.getX() - movementSpeed - WIDTH / 2f;

        if (yCheck >= 0 && xCheck >= 0) {
            location = location.add(-movementSpeed, -movementSpeed);
        }
    }

    public void moveDown(double boardWidth, double boardHeight) {

        double yCheck = location.getY() + movementSpeed + WIDTH / 2f;
        double xCheck = location.getX() + movementSpeed + WIDTH / 2f;

        if (yCheck <= boardHeight && xCheck <= boardWidth) {
            location = location.add(movementSpeed, movementSpeed);
        }
    }

    public void moveLeft(double boardWidth) {
        double xCheck = location.getX() - movementSpeed - WIDTH / 2f;
        double yCheck = location.getY() + movementSpeed + WIDTH / 2f;

        if (xCheck >= 0 && yCheck <= boardWidth) {
            location = location.add(-movementSpeed, movementSpeed);
        }
    }

    public void moveRight(double boardWidth, double boardHeight) {
        //check within bounds

        double xCheck = location.getX() + movementSpeed + WIDTH / 2f;
        double yCheck = location.getY() - movementSpeed - WIDTH / 2f;


        if (xCheck <= boardWidth && yCheck >= 0) {
            location = location.add(movementSpeed, -movementSpeed);
        }
    }


    public void moveUpCartesian() {
        if ((location.getY() - movementSpeed - WIDTH / 2f) >= 0) {
            location = location.add(0, -(movementSpeed * 2));
        } else {
            location = new Point2D(location.getX(), 0 + WIDTH / 2f);
        }
    }

    public void moveDownCartestian(double boardHeight) {

        if ((location.getY() + movementSpeed + WIDTH / 2f) <= boardHeight) {
            location = location.add(0, (movementSpeed * 2));
        } else {
            location = new Point2D(location.getX(), boardHeight - WIDTH / 2f);
        }

    }

    public void moveLeftCartesian() {
        //check within bounds
        if ((location.getX() - movementSpeed - WIDTH / 2f) >= 0) {
            location = location.add(-(movementSpeed * 2), 0);
        } else {
            location = new Point2D(0 + WIDTH / 2f, location.getY());
        }

    }

    public void moveRightCartesian(double boardWidth) {
        //check within bounds
        if ((location.getX() + movementSpeed + WIDTH / 2f) <= boardWidth) {
            location = location.add((movementSpeed * 2), 0);
        } else {
            location = new Point2D(boardWidth - WIDTH / 2f, location.getY());
        }
    }


    public Rotate getPlayerAngle() {
        return playerAngle;
    }

    public void setPlayerAngle(Rotate playerAngle) {
        this.playerAngle = playerAngle;
    }


    public Point2D getLocation() {
        return location;
    }

    public void setLocation(Point2D location) {
        this.location = location.add(WIDTH / 2f, WIDTH / 2f);
    }

    public int getWidth() {
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

    public Rectangle getHitBox() {
        return new Rectangle(location.getX(),location.getY(),WIDTH,WIDTH);
    }

    public PlayerStates getState() {
        return state;
    }

    public boolean getIsShielded(){
        return isShielded;
    }



}
