package graphics.userInterface.controllers;

import graphics.userInterface.resources.style.Shadow;
import javafx.fxml.Initializable;
import javafx.scene.effect.Blend;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UIController implements Initializable{
    protected Stage stage;
    protected Blend blend;
    protected Color outline;

    UIController(){
        //javafx8 doesn't support to define shadow in css while javafx11 supports
        Shadow shadow = new Shadow();
        blend = shadow.loadShadow();
        outline = Color.WHITE;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
