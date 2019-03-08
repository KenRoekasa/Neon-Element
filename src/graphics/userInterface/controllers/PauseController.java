package graphics.userInterface.controllers;

import client.ClientGameState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

//Press space to pause the game
public class PauseController extends UIController{
    @FXML
    Label resume,option,quit;
    private Rectangle stageSize;
    private Pane hudPane;

    public void setNode(Pane node) {
        this.node = node;
    }

    private  Pane node;

    public void setStageSize(Rectangle stageSize) {
        this.stageSize = stageSize;
    }

    public void setHudPane(Pane hudPane) {
        this.hudPane = hudPane;
    }

    private ClientGameState gameState;

    @FXML
    public void setStage(Stage stage, ClientGameState gameState) {
        this.gameState = gameState;
        super.stage = stage;
    }

    @FXML
    public void handleResumeBtn(){
        hudPane.getChildren().remove(node);
    }

    // todo this needs to return to the game, probably want an options menu thats transparent like the pause menu
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
            controller.setNode(subnode);
            controller.setStage(stage);
            stage.setTitle("Sound");
        } catch (IOException e) {
            System.out.println("Crush in loading setting board ");
            e.printStackTrace();
        }
    }

    @FXML
    // todo this needs to end the game thread somehow
    public void handleQuitBtn(){
        String fxmlPath = "../fxmls/menu.fxml";
        String stageTitle = "Menu";
        String fileException ="Menu";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException);
        super.stage.getScene().setCursor(Cursor.DEFAULT);
        gameState.stop();
    }
}
