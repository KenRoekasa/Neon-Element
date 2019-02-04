package server;

import server.ServerGameState;

public class GameServer extends Thread {

    private ServerGameState gameState;

    public GameServer(ServerGameState gameState) {
        this.gameState = gameState;
    }

    public ServerGameState getGameState() {
        return gameState;
    }

}
