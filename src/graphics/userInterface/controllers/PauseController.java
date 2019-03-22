package graphics.userInterface.controllers;

import client.ClientGameState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.IOException;


/**
 * Controller for pause.fxml
 * <p>Pressing p in the game will start the pause function</p>
 */
//Press 'P' to pause the game
public class PauseController extends UIController{
    /**
     * The size of stage
     */
    private Rectangle stageSize;
    /**
     * The pane of hud
     */
    private Pane hudPane;
    /**
     * The node pane
     */
    private  Pane node;
    /**
     * Current game state
     */
    private ClientGameState gameState;

    /** Set the node
     * @param node subnode of scene
     */
    public void setNode(Pane node) {
        this.node = node;
    }

    /**Set size of the current stage
     * @param stageSize the size of the current stage
     */
    public void setStageSize(Rectangle stageSize) {
        this.stageSize = stageSize;
    }

    /** Set hud pane
     * @param hudPane hud pane of game
     */
    public void setHudPane(Pane hudPane) {
        this.hudPane = hudPane;
    }

    /** Set the stage of current scene
     * @param stage the stage of current game
     * @param gameState the current game state
     */
    public void setStage(Stage stage, ClientGameState gameState) {
        this.gameState = gameState;
        super.stage = stage;
    }

    /**
     * Handle the action of pressing resume button which will resume back to the game board
     */
    @FXML
    public void handleResumeBtn(){
        hudPane.getChildren().remove(node);
        gameState.resume();
    }

    /**
     * Handle the action of pressing setting button which will go to options_game.fxml
     */
    @FXML
    public void handleSettingBtn(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/options_game.fxml"));

        try {
            hudPane.getChildren().remove(node);
            Pane subnode = loader.load();
            subnode.setPrefHeight(stageSize.getHeight());
            subnode.setPrefWidth(stageSize.getWidth());
            hudPane.getChildren().add(subnode);
            subnode.setBackground(Background.EMPTY);
            GameOptionsController controller = loader.getController();
            controller.setHudPane(hudPane);
            controller.setGamestate(gameState);
            controller.setAudioManager(audioManager);
            controller.setNode(subnode);
            controller.setStage(stage);
            controller.updateVolume();
            stage.setTitle("Sound");

        } catch (IOException e) {
            System.out.println("Crush in loading sound board ");
            e.printStackTrace();
        }
    }

    /**
     * Handle the action of pressing quit button which will shut down the game
     */
    @FXML
    // todo this needs to end the game thread somehow
    public void handleQuitBtn(){
        String fxmlPath = "../fxmls/menu.fxml";
        String stageTitle = "Menu";
        String fileException ="Menu";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
        super.stage.getScene().setCursor(Cursor.DEFAULT);
        gameState.stop();
        audioManager.setMenuMusic();
        audioManager.setNeonVolume(audioManager.getEffectVolume());
    }
}
