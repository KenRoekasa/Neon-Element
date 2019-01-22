package Entities;


import Enums.PowerUpType;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class PowerUp {

    private PowerUpType type;

    private Rectangle hitbox = new Rectangle(10, 10);

    public PowerUp() {
        //Randomise the type of powerUp when it spawns
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
    }

    public void activate(){

    }



    public PowerUpType getType() {
        return type;
    }


    public Rectangle getHitbox() {
        return hitbox;
    }
}
