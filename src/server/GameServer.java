package server;

import engine.GameTypeHandler;
import engine.Physics;
import engine.entities.Player;
import javafx.geometry.Point2D;
import networking.server.ConnectedPlayers;
import networking.server.ServerNetwork;
import server.controllers.PowerUpController;

public class GameServer extends Thread {

    private ServerGameState gameState;
    private ServerNetwork network;
    private Physics physicsEngine;

    private boolean running;

    private int expectedPlayersToJoin = 2;

    public GameServer(ServerGameState gameState) {
        this.gameState = gameState;
        this.network = new ServerNetwork(this.gameState);
        this.physicsEngine = new Physics(gameState);
    }

    public void run() {
        this.network.start();

        this.waitForPlayersToConnect();

        System.out.println("Game started.");

        Thread powerUpController = new Thread(new PowerUpController(gameState, this.network.getDispatcher()));
        powerUpController.start();

        this.running = true;
        while (this.running) {
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

    private void waitForPlayersToConnect() {
        ConnectedPlayers connectedPlayers = this.network.getConnectedPlayers();

        // Wait for enough players to start the game
        while (connectedPlayers.count() < expectedPlayersToJoin) {
            Thread.yield();
        }

        // Start the game
        connectedPlayers.assignStartingLocations(gameState.getMap().getWidth(), gameState.getMap().getHeight());
        this.gameState.getScoreBoard().initialise(this.gameState.getAllPlayers());
        this.network.getDispatcher().broadcastGameState();

        this.gameState.setStarted(true);
        this.network.getDispatcher().broadcastGameStarted();
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
