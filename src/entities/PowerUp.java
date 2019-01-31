package entities;


import enums.PowerUpType;
import javafx.geometry.Point2D;

import java.util.Random;

public class PowerUp extends PhysicsObject {

    private PowerUpType type;
    private Player player;
    private boolean isActive = true;


    public PowerUp(Player player) {
        width = 10;

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

        int randX = rand.nextInt(200);
        int randY = rand.nextInt(200);

        //Todo: chage to randX randY
        location = new Point2D(0, 0);


        this.player = player;
    }

    public void setLocation(Point2D location) {
        this.location = location;
    }

    public void activatePowerUp(){
        if(isActive){
            System.out.println("Power up is picked up");
            isActive = false;
        }
    }


    public PowerUpType getType() {
        return type;
    }


    @Override
    public void update() {

    }
}

