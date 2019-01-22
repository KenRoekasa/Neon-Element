package Entities;

import Calculations.DamageCalculation;
import Enums.PlayerStates;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;


public class Player {
    private Point2D location = new Point2D(0, 0);
    private Rotate rotation = new Rotate(0);
    private final int WIDTH = 20;


    private float health = 100;
    private final float MAX_HEALTH = 100;
    private int movementSpeed = 10;
    private boolean isShielded = false;


    //Default Fire
    private PlayerStates state = PlayerStates.FIRE;

    private Rectangle hitBox = new Rectangle(WIDTH, WIDTH);
    private Rectangle attackHitbox = new Rectangle(WIDTH, WIDTH);

    //COOLDOWNS
    //The number of seconds for change state to go off cooldown
    private final float CHANGESTATECD = 1.2f;
    // The countdown of the changestate cooldown
    private float changeStateCurrentCD;

    private DamageCalculation dmgCal = new DamageCalculation();

    private void lightAttack() {
        int damage = 3;

        //set attack hit box in front of the user
        //TODO: Change hitbox location based on rotation too
        attackHitbox.setX(location.getX() + WIDTH);
        attackHitbox.setY(location.getX() + WIDTH);



        Player otherPlayer[] = new Player[4];



        //If another player is in the hitbox calculate the damage they take
        for (Player p : otherPlayer) {
            if (attackHitbox.intersects(p.getHitBox().getBoundsInParent())) {
                p.removeHealth(dmgCal.calculateDamage(damage,this, p));
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


    public void updateHitbox() {
        hitBox.setX(location.getX());
        hitBox.setY(location.getY());
    }


    public void addPowerup() {


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

    public void setLocation(Point2D location) {
        this.location = location.add(WIDTH / 2f, WIDTH / 2f);
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

    public Rectangle getHitBox() {
        return hitBox;
    }

    public PlayerStates getState() {
        return state;
    }

    public boolean getIsShielded(){
        return isShielded;
    }

}
