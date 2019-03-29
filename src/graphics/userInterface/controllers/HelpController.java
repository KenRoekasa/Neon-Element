package graphics.userInterface.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.paint.Color.rgb;

/**
 * Controller for help.fxml
 */
public class HelpController extends UIController {

    /**
     * Panes for storing different introductions
     */
    Pane mode_pane,control_pane,rule_pane;
    /**
     * Toggle group of selectable buttons
     */
    ToggleGroup menu = new ToggleGroup();

    /**
     * Stack pane to put the panes
     */
    @FXML
    StackPane stack_pane;
    /**
     * Radio button for choosing help options
     */
    @FXML
    RadioButton mode_btn,control_btn,rule_btn,back;

    /**
     * Show modes introduction
     */
    @FXML
    public void showMode(){
        mode_pane.setVisible(true);
        control_pane.setVisible(false);
        rule_pane.setVisible(false);

    }

    /**
     * Show controls introduction
     */
    @FXML
    public void showControl(){
        mode_pane.setVisible(false);
        control_pane.setVisible(true);
        rule_pane.setVisible(false);

    }

    /**
     * Show rules introduction
     */
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

    /**
     * Initialise the stack pane and toggle group binding
     * @param location  url location
     * @param resources resource bundled
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXMLLoader modeLoader = new FXMLLoader(getClass().getResource("../fxmls/help-mode.fxml"));
        FXMLLoader controlLoader = new FXMLLoader(getClass().getResource("../fxmls/help-control.fxml"));
        FXMLLoader ruleLoader = new FXMLLoader(getClass().getResource("../fxmls/help-rule.fxml"));

        try {
            mode_pane = modeLoader.load();
            control_pane = controlLoader.load();
            rule_pane = ruleLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stack_pane.getChildren().add(mode_pane);
        stack_pane.getChildren().add(control_pane);
        stack_pane.getChildren().add(rule_pane);


        control_pane.setVisible(false);
        rule_pane.setVisible(false);

       ToggleGroupSetUp.setToggleGroup(menu,mode_btn,control_btn,rule_btn,back);

        mode_btn.setSelected(true);


    }
}



