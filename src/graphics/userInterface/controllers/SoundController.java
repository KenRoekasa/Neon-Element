package graphics.userInterface.controllers;

import client.ClientGameState;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

/**
 * Controller for sound.fxml which in the options of pause
 */
public class SoundController extends UIController{
    /**
     * The hud pane
     */
    private Pane hudPane;
    /**
     * The node pane
     */
    private Pane node;
    /**
     * The current game state
     */
    private ClientGameState gamestate;

    /** Set the hud pane
     * @param hudPane
     */
    public void setHudPane(Pane hudPane) {
        this.hudPane = hudPane;
    }

    /** Set the subnode pane
     * @param node
     */
    public void setNode(Pane node) {
        this.node = node;
    }

    /** Set the game state
     * @param gameState the game state of the game
     */
    public void setGamestate(ClientGameState gameState) {
        this.gamestate = gameState;
    }

    /**
     * Handle the action of pressing ok button which will send the volume back to audio manager
     */
    @FXML
    public void handleOkBtn(){

    }
    /**
     * Handle the action of pressing back button which will go to game board
     */
    @FXML
    public void handleBackBtn(){
        hudPane.getChildren().remove(node);
        gamestate.resume();

    }

}

