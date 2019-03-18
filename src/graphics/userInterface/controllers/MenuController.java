package graphics.userInterface.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;


/**
 *
 */
public class MenuController extends UIController implements Initializable{
    /**
     * Buttons of play, help, option and exit
     */
    @FXML
    public Button play,help,option,exit;

    /** Handle the action when pressing play button which direct to mode.fxml
     */
    // play -> mode selection
    @FXML
    public void handlePlayBtn() {

        // create game rules
        // todo make this configurable
        //select mode
        String fxmlPath = "../fxmls/mode.fxml";
        String stageTitle = "Mode";
        String fileException ="Mode";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }

    /** Handle the action when pressing option button which direct to option.fxml
     */
    @FXML
    public void handleOptionBtn(){
        String fxmlPath ="../fxmls/option.fxml";
        String stageTitle ="Option Setup" ;
        String fileException ="Option";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }


    /** Handle the action when pressing help button which direct to help.fxml
     */
    @FXML
    public void handleHelpBtn(){
        String fxmlPath ="../fxmls/help.fxml";
        String stageTitle ="Tutorial" ;
        String fileException ="Help";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);

    }

    /** Handle the action when pressing exit button which shut down the game
     */
    @FXML
    public void handleExitBtn(){
        stage.close();

        // todo make this graceful

        Platform.exit();
        System.exit(0);
    }

}
