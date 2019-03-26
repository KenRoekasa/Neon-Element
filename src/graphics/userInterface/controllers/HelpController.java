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


    Pane mode_pane,control_pane,rule_pane;

    ToggleGroup menu = new ToggleGroup();

    @FXML
    StackPane stack_pane;
    @FXML
    RadioButton mode_btn,control_btn,rule_btn,back;


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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXMLLoader modeLoader = new FXMLLoader(getClass().getResource("../fxmls/help_mode_new.fxml"));
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



