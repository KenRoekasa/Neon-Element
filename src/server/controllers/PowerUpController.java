package server.controllers;

import engine.model.GameState;
import engine.entities.PhysicsObject;
import engine.entities.PowerUp;
import networking.server.ServerNetworkDispatcher;

import java.util.ArrayList;

public class PowerUpController implements Runnable {

    private GameState gamestate;
    private ArrayList<PhysicsObject> objects;
    private ServerNetworkDispatcher dispatcher;

    public PowerUpController(GameState gameState) {
        this.objects = gameState.getObjects();
        this.gamestate = gameState;
    }

    public PowerUpController(GameState gameState, ServerNetworkDispatcher dispatcher) {
        this.objects = gameState.getObjects();
        this.gamestate = gameState;
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        // creates a power up every 15 sec
        while (gamestate.getRunning()) {
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
