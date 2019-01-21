package Entities;

import Enums.PlayerStates;
import javafx.geometry.Point2D;


public class Player {
    private Point2D location = new Point2D(0,0);
    private final int WIDTH = 20;
    private int movementSpeed = 10;
    //Can be a float
    private int health =100;
    private final int MAX_HEALTH = 100;
    private PlayerStates state;



    private void lightAttack(){
        switch (state){
            case AIR:
                //attack using air
                break;
            case FIRE:
                //attack using fire
                break;
            case EARTH:
                //attack using earth
                break;
            case WATER:
                //attack using water
                break;
            default:
                //What should happen?
                break;
        }

    }

    private void heavyAttack(){
        switch (state){
            case AIR:
                //attack using air
                break;
            case FIRE:
                //attack using fire
                break;
            case EARTH:
                //attack using earth
                break;
            case WATER:
                //attack using water
                break;
            default:
                //What should happen?
                break;
        }

    }

    private void shield(){
        switch (state){
            case AIR:
                //attack using air
                break;
            case FIRE:
                //attack using fire
                break;
            case EARTH:
                //attack using earth
                break;
            case WATER:
                //attack using water
                break;
            default:
                //What should happen?
                break;
        }

    }

    //teleport a certain amount in front the character or we could have a speed boost
    private void dash(){


    }


    public void moveUp(){
        location.add(0,-movementSpeed);
    }

    public void moveDown(){

        location.add(0,movementSpeed);

    }

    public void moveLeft(){
        location.add(-movementSpeed,0);
    }

    public void moveRight(){
        location.add(0, movementSpeed);
    }






    public Point2D getLocation(){
        return location;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public int getHealth() {
        return health;
    }

    public int getMAX_HEALTH() {
        return MAX_HEALTH;
    }


}
