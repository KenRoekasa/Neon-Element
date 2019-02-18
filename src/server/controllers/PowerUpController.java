package server.controllers;

import engine.entities.PhysicsObject;
import engine.entities.PowerUp;
import networking.server.ServerNetworkDispatcher;

import java.util.ArrayList;

public class PowerUpController implements Runnable {

    ArrayList<PhysicsObject> objects;
    ServerNetworkDispatcher dispatcher;

    public PowerUpController(ArrayList<PhysicsObject> objects) {
        this.objects = objects;
    }
    
    public PowerUpController(ArrayList<PhysicsObject> objects, ServerNetworkDispatcher dispatcher) {
        this.objects = objects;
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        // creates a power up every 15 sec
        while (true) {
            synchronized (objects) {
                PowerUp powerUp = new PowerUp();
                objects.add(powerUp);
//                this.dispatcher.broadcastNewPowerUp(powerUp);
            }
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
