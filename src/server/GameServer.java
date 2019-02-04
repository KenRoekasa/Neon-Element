package server;

import networking.server.ServerNetwork;
import server.ServerGameState;

public class GameServer extends Thread {

    private ServerGameState gameState;
    private ServerNetwork network;

    public GameServer(ServerGameState gameState) {
        this.gameState = gameState;
        this.network = new ServerNetwork(this.gameState);
    }

    public ServerGameState getGameState() {
        return gameState;
    }

}
