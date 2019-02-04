package server;

import networking.server.ServerNetwork;
import server.ServerGameState;

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

        while(this.running) {
            // Server logic
        }

        this.network.close();
    }

}
