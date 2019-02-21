package graphics.userInterface.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javax.swing.border.EmptyBorder;
import java.io.IOException;

//Press space to pause the game
public class PauseController {
    private Stage stage;
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

    @FXML

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void handleResumeBtn(){
        hudPane.getChildren().remove(node);
    }

    //back to menu or game?
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
            stage.setTitle("Setting");
        } catch (IOException e) {
            System.out.println("crush in loading setting board ");
            e.printStackTrace();
        }
    }

    @FXML
    // todo this needs to end the game thread somehow
    public void handleQuitBtn(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/menu.fxml"));
        try {
            Pane root = loader.load();
            stage.getScene().setRoot(root);
            MenuController controller = loader.getController();
            controller.setStage(stage);
            stage.setTitle("Menu");

        } catch (IOException e) {
            System.out.println("crush in loading menu board ");
            e.printStackTrace();
        }
    }

}
