package controllers;

import client.ClientBoard;
import client.ClientGameState;
import client.GameStateGenerator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

//For local_setup scene
public class LocalController {
    private ClientGameState gameState;
    private Stage stage;
    private Rectangle2D stageSize;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setStageSize(Rectangle2D stageSize) {
        this.stageSize = stageSize;
    }

    // directly go to local mode map
    @FXML
    public void handleStartBtn(){
        // create game rules
        // todo make this configurable
            gameState = GameStateGenerator.createDemoGamestate();
        //g.getPlayer().getHealth();
        try {
            ClientBoard gameBoard = new ClientBoard(stage, gameState);
            Scene scene = gameBoard.getScene();

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    @FXML
    public void handleBackBtn(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/mode_board.fxml"));

        try {
            Pane root = loader.load();
            stage.getScene().setRoot(root);
            ModeController modeController = loader.getController();
            modeController.setStage(stage);
            modeController.setStageSize(stageSize);
            stage.setTitle("Local Mode");
            
        } catch (IOException e) {
            System.out.println("crush in loading mode board ");
            e.printStackTrace();
        }
    }

}

