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


//Press 'P' to pause the game
public class PauseController extends UIController{
    private Rectangle stageSize;
    private Pane hudPane;
    private  Pane node;
    private ClientGameState gameState;

    public void setNode(Pane node) {
        this.node = node;
    }
    public void setStageSize(Rectangle stageSize) {
        this.stageSize = stageSize;
    }

    public void setHudPane(Pane hudPane) {
        this.hudPane = hudPane;
    }

    public void setStage(Stage stage, ClientGameState gameState) {
        this.gameState = gameState;
        super.stage = stage;
    }

    @FXML
    public void handleResumeBtn(){
        hudPane.getChildren().remove(node);
        gameState.resume();
    }

    @FXML
    public void handleSettingBtn(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/sound.fxml"));

        try {
            hudPane.getChildren().remove(node);
            Pane subnode = loader.load();
            subnode.setPrefHeight(stageSize.getHeight());
            subnode.setPrefWidth(stageSize.getWidth());
            hudPane.getChildren().add(subnode);
            subnode.setBackground(Background.EMPTY);
            SoundController controller = loader.getController();
            controller.setHudPane(hudPane);
            controller.setGamestate(gameState);
            controller.setNode(subnode);
            controller.setStage(stage);
            stage.setTitle("Sound");

        } catch (IOException e) {
            System.out.println("Crush in loading sound board ");
            e.printStackTrace();
        }
    }

    @FXML
    // todo this needs to end the game thread somehow
    public void handleQuitBtn(){
        String fxmlPath = "../fxmls/menu.fxml";
        String stageTitle = "Menu";
        String fileException ="Menu";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
        super.stage.getScene().setCursor(Cursor.DEFAULT);
        gameState.stop();
    }
}
