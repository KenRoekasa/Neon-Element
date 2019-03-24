package graphics.userInterface.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

/**
 * Controller for help.fxml
 */
public class HelpController extends UIController {


    @FXML
    GridPane mode_pane,control_pane,rule_pane;


    @FXML
    public void showMode(){
        mode_pane.setVisible(true);
        control_pane.setVisible(false);
        rule_pane.setVisible(false);

    }

    @FXML
    public void showControl(){
        mode_pane.setVisible(false);
        control_pane.setVisible(true);
        rule_pane.setVisible(false);

    }

    @FXML
    public void showRule(){
        mode_pane.setVisible(false);
        control_pane.setVisible(false);
        rule_pane.setVisible(true);
    }

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



