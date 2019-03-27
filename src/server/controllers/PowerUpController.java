package server.controllers;

import client.GameClient;
import engine.entities.PhysicsObject;
import engine.entities.PowerUp;
import engine.model.GameState;
import networking.server.ServerNetworkDispatcher;

import java.util.ArrayList;

public class PowerUpController {

    private GameState gamestate;
    private ArrayList<PhysicsObject> objects;
    private ServerNetworkDispatcher dispatcher;
    private long lastTime;

    public PowerUpController(GameState gameState) {
        this.gamestate = gameState;
        this.objects = gamestate.getObjects();
        lastTime = GameClient.timeElapsed;
    }

    public PowerUpController(GameState gameState, ServerNetworkDispatcher dispatcher) {
        this.gamestate = gameState;
        this.objects = gamestate.getObjects();
        this.dispatcher = dispatcher;

    }


    public void serverUpdate(){
        long currentTime = GameClient.timeElapsed;
        if(currentTime-lastTime >= 5000){

            PowerUp powerUp = new PowerUp();
            objects.add(powerUp);
            lastTime = GameClient.timeElapsed;
            this.dispatcher.broadcastNewPowerUp(powerUp);
        }
    }


    public void update() {
        long currentTime = GameClient.timeElapsed;

        if(currentTime-lastTime >= 5000){


            PowerUp powerUp = new PowerUp();
            objects.add(powerUp);
            lastTime = GameClient.timeElapsed;
        }
    }


}
