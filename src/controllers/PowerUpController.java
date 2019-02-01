package controllers;

import entities.PhysicsObject;
import entities.PowerUp;

import java.util.ArrayList;

public class PowerUpController implements Runnable {
    ArrayList<PhysicsObject> objects;

    public PowerUpController(ArrayList<PhysicsObject> objects){
        this.objects = objects;
    }
    @Override
    public void run() {
        // creates a power up every 2 sec
        while(true){
            objects.add(new PowerUp());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
