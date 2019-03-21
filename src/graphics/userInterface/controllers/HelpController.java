package graphics.userInterface.controllers;

import javafx.fxml.FXML;

/**
 * Controller for help.fxml
 */
public class HelpController extends UIController {

    /**
     * Handle the action of pressing help button which will direct to help.fxml
     */
    @FXML
    public void handleBackBtn() {
        String fxmlPath = "../fxmls/menu.fxml";
        String stageTitle = "Menu";
        String fileException = "Menu";
        FxmlLoader loader = new FxmlLoader(fxmlPath, stage, stageTitle, fileException, audioManager);
    }

}
