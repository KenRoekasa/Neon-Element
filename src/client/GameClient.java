package client;

import networking.client.ClientNetwork;
import client.ClientGameState;

public class GameClient {

    private ClientGameState gameState;
    private ClientNetwork network;

    public GameClient(ClientGameState gameState) {
        this.gameState = gameState;
        this.network = new ClientNetwork(this.gameState);
    }

}
