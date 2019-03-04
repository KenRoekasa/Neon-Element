package graphics.userInterface.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameOverController extends UIController{
    @FXML
    Label back;

    public void handleQuitBtn(ActionEvent actionEvent) {
        String fxmlPath = "../fxmls/menu_new.fxml";
        String stageTitle = "Menu";
        String fileException ="Menu";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException);
    }

}
