package Entities;


import Enums.PowerUpType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class PowerUp extends Thread{

    private Point2D location;

    private final int WIDTH = 10;

    private PowerUpType type;
    private Player player;



    public PowerUp(Player player) {
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

        location = new Point2D(0, 0);


        this.player = player;
    }


    public Point2D getLocation() {
        return location;
    }

    public void setLocation(Point2D location) {
        this.location = location;
    }


    public int getWIDTH() {
        return WIDTH;
    }


    public PowerUpType getType() {
        return type;
    }


    public Rectangle getHitbox() {
        return new Rectangle(location.getX(),location.getY(),WIDTH, WIDTH);
    }

    @Override
    public void run() {
        while(true){
            //checks if the player has picked it up or not
                if(getHitbox().intersects(player.getHitBox().getBoundsInParent())){
                    System.out.println("Collided with pick up");
                    player.addBuff(this);
                    break;
                    //TODO: Code what to do when pick up is picked up


            }
        }
    }
}
