package server;

import engine.controller.GameTypeHandler;
import engine.physics.CollisionDetector;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.entities.PowerUp;
import engine.model.enums.Action;
import engine.model.enums.ObjectType;
import javafx.geometry.Point2D;
import networking.server.ServerNetwork;
import server.controllers.PowerUpController;

import java.util.ArrayList;
import java.util.Iterator;

public class GameServer extends Thread {

    private ServerGameState gameState;
    private ServerNetwork network;

    private boolean running;

    public GameServer(ServerGameState gameState) {
        this.gameState = gameState;
        this.network = new ServerNetwork(this.gameState);
    }

    public void run() {
        this.running = true;
        this.network.start();



        while(this.running) {
            // Server logic


            this.running = GameTypeHandler.checkRunning(gameState);

            Thread.yield();
            this.sendLocations();

            try {
                Thread.sleep(1000L); // Every second
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        this.network.close();
    }

    
    private void sendLocations() {
        synchronized (gameState.getAllPlayers()) {
            for (Player p : gameState.getAllPlayers()) {
                Point2D location = p.getLocation();
                double x = location.getX();
                double y = location.getY();

                this.network.getDispatcher().broadcastLocationState(p.getId(), x, y);
            }
        }
    }

}
