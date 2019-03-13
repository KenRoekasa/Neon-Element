package engine.entities;


import engine.enums.ObjectType;
import engine.enums.PowerUpType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.Random;

public class PowerUp extends PhysicsObject {

    private static int nextId = 0;
    /**
     * The unique identifier of this powerup
     */
    private int id;
    /**
     * The type of the power heal,damage or speed
     */
    private PowerUpType type;
    /**
     * If the powerup can be picked up or not
     */
    private boolean isActive = true;

    /**
     * Constructor
     */
    public PowerUp() {
        id = nextId++;
        tag = ObjectType.POWERUP;
        width = tag.getSize();

        //Randomise the type of power up when it spawns
        Random rand = new Random();
        int typeInt = rand.nextInt(3);

        switch (typeInt) {
            case 0:
                type = PowerUpType.HEAL;
                break;
            case 1:
                type = PowerUpType.SPEED;
                break;
            case 2:
                type = PowerUpType.DAMAGE;
                break;
            default:
                break;
        }


        //TODO: Change Values to be based on the map
        int randX = rand.nextInt(2000);
        int randY = rand.nextInt(2000);

        location = new Point2D(randX, randY);
    }

    /**
     * Constructor
     *
     * @param id the id of the power up
     * @param x  x coordinate of the power up
     * @param y  y coordinate of the power up
     */
    public PowerUp(int id, double x, double y) {
        this.id = id;
        this.location = new Point2D(x, y);
    }

    public int getId() {
        return this.id;
    }

    public Point2D getLocation() {
        return this.location;
    }

    public void setLocation(Point2D location) {
        this.location = location;
    }

    /**
     * Activate the power and cause an effect on the player
     *
     * @param player The player you want to be effected by the powerup
     */
    public void activatePowerUp(Character player) {
        if (isActive) {
            switch (type) {
                case HEAL:
                    player.addHealth(10);
                    break;
                case SPEED:
                    player.speedBoost();
                    break;
                case DAMAGE:
                    player.damageBoost();
                    break;
                default:
                    break;
            }
            isActive = false;
        }
    }


    public PowerUpType getType() {
        return type;
    }


    @Override
    public void update() {

    }

    @Override
    public Shape getBounds() {
        return new Circle(location.getX() + width, location.getY() + width, width);
    }
}

