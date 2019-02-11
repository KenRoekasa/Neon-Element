package server;

import networking.server.ServerNetwork;
import server.ServerGameState;
import server.controllers.PowerUpController;

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

        Thread powerUpController = new Thread(new PowerUpController(gameState.getObjects(), this.network.getDispatcher()));
        powerUpController.start();

        while(this.running) {
            // Server logic
        }

        this.network.close();
    }

}
