package entities;


import enumSwitches.objectSize;
import enums.ObjectType;
import enums.PowerUpType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.Random;

public class PowerUp extends PhysicsObject {

    private PowerUpType type;

    private boolean isActive = true;


    public PowerUp() {
        tag = ObjectType.POWERUP;
        width = objectSize.getObjectSize(tag);

        //Randomise the type of power up when it spawns
        Random rand = new Random();
        int typeInt = rand.nextInt(2);
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
        int randX = rand.nextInt(1500);
        int randY = rand.nextInt(1500);

        location = new Point2D(randX, randY);

    }

    public void setLocation(Point2D location) {
        this.location = location;
    }

    public void activatePowerUp(Player player) {
        if (isActive) {
            System.out.println("Power up is picked up");
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
        return new Circle(location.getX() + width, location.getY() + width, width / 2f);
    }
}

