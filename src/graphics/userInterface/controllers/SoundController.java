package graphics.userInterface.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class SoundController extends UIController{
    private Pane hudPane;
    private Pane node;

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

