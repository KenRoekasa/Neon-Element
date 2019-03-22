package graphics.userInterface.controllers;

import client.GameClient;

public class LobbyJoinController extends UIController{
    public void setGameClient(GameClient gameClient) {
        this.gameClient = gameClient;
    }

    private GameClient gameClient;

    public void startGame(){
        gameClient.initialiseGame();
    }

}
