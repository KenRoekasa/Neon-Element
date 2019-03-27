package graphics.userInterface.controllers;

import client.GameClient;

public abstract class AbstractLobbyController extends UIController {

    public void setGameClient(GameClient gameClient) {
        this.gameClient = gameClient;
    }

    public GameClient getGameClient() {
        return this.gameClient;
    }

    private GameClient gameClient;

    public void startGame(){
        gameClient.initialiseGame();
    }
}
