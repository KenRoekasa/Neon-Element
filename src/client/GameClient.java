package client;

import networking.client.ClientNetwork;
import client.ClientGameState;

public class GameClient extends Thread {

    private ClientGameState gameState;
    private ClientNetwork network;

    private boolean running;

    public GameClient(ClientGameState gameState) {
        this.gameState = gameState;
        this.network = new ClientNetwork(this.gameState);
    }

    public void run() {
        this.running = true;

        while(this.running) {
            // Client logic
        }

        this.network.close();
    }


}
