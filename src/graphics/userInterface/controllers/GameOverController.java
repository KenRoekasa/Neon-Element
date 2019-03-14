package graphics.userInterface.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller for gameover.fxml
 */
public class GameOverController extends UIController{
    @FXML
    Label back;

    /**
     * Handle the action of pressing back button which will go back to menu.fxml
     */
    public void handleQuitBtn() {
        String fxmlPath = "../fxmls/menu.fxml";
        String stageTitle = "Menu";
        String fileException ="Menu";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }

}
