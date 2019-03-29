package graphics.userInterface.controllers;

import client.GameClient;

/**
 * Abstract Lobby controller for lobby controller
 */
public abstract class AbstractLobbyController extends UIController {

    /**
     * Current game client
     */
    private GameClient gameClient;

    /**
     * Set the game client
     * @param gameClient
     */
    public void setGameClient(GameClient gameClient) {
        this.gameClient = gameClient;
    }

    /**
     * Get the game client
     * @return current game client
     */
    public GameClient getGameClient() {
        return this.gameClient;
    }

    /**
     * Start game client initialise
     */
    public void startGame(){
        gameClient.initialiseGame();
    }
}
