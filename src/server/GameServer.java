package server;

import java.util.ArrayList;
import java.util.Iterator;

import engine.GameTypeHandler;
import engine.Physics;
import engine.entities.CollisionDetection;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.entities.PowerUp;
import engine.enums.Action;
import engine.enums.ObjectType;
import javafx.geometry.Point2D;
import javafx.scene.transform.Rotate;
import networking.server.ServerNetwork;
import server.controllers.PowerUpController;

public class GameServer extends Thread {

    private ServerGameState gameState;
    private ServerNetwork network;
    private Physics physicsEngine;

    private boolean running;

    public GameServer(ServerGameState gameState) {
        this.gameState = gameState;
        this.network = new ServerNetwork(this.gameState);
        this.physicsEngine = new Physics(gameState);
    }

    public void run() {
        this.network.start();

        // Wait for game to be started
        while(!this.gameState.isStarted()) {
            Thread.yield();
        }

        System.out.println("Game started.");

        Thread powerUpController = new Thread(new PowerUpController(gameState, this.network.getDispatcher()));
        powerUpController.start();

        this.running = true;
        while(this.running) {
            // Server logic
            physicsEngine.clientLoop();

            this.running = GameTypeHandler.checkRunning(gameState);

            Thread.yield();
            this.sendLocations();

            try {
                Thread.sleep(25); // Every second
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
                Rotate playerAngle = p.getPlayerAngle();
                double x = location.getX();
                double y = location.getY();

                this.network.getDispatcher().broadcastLocationState(p.getId(), x, y, playerAngle.getAngle());
            }
        }
    }

}
