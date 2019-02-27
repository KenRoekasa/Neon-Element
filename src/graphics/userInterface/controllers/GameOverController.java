package graphics.userInterface.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class GameOverController extends UIController{

    public void handleQuitBtn(ActionEvent actionEvent) {
        String fxmlPath = "../fxmls/menu_new.fxml";
        String stageTitle = "Menu";
        String fileException ="Menu";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException);
    }
}
