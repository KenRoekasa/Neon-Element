package graphics.userInterface.controllers;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UIController implements Initializable{
    protected Stage stage;

    UIController(){
        //javafx8 doesn't support to define shadow in css while javafx11 supports

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
