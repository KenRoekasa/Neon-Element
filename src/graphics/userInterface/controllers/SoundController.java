package graphics.userInterface.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class SoundController  {
    private Stage stage;
    private Pane hudPane;
    private Pane node;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setHudPane(Pane hudPane) {
        this.hudPane = hudPane;
    }

    public void setNode(Pane node) {
        this.node = node;
    }

    @FXML
    public void handleOkBtn(){


    }
    @FXML
    public void handleBackBtn(){
        hudPane.getChildren().remove(node);
    }
}
