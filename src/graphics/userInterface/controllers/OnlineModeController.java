package graphics.userInterface.controllers;

import javafx.fxml.FXML;

/**
 * Controller for online_mode.fxml
 */
public class OnlineModeController extends UIController{

    /** Handle the action of pressing host button which will direct to online_setup.fxml
     */
    @FXML
    public void handleHostGame(){
        String fxmlPath = "../fxmls/online_setup.fxml";
        String stageTitle = "Online Mode Configuration";
        String fileException ="Online Setup";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }

    /** Handle the action of pressing join button which will direct to ip_host.fxml
     */
    //todo create game client and go to game lobby
    @FXML
    public void handleJoinGame() {
        String fxmlPath = "../fxmls/ip_join.fxml";
        String stageTitle = "Join";
        String fileException = "ip_join ";
        FxmlLoader loader = new FxmlLoader(fxmlPath, stage, stageTitle, fileException, audioManager);
    }


    /** Handle the action of pressing back button which will go back to mode.fxml
     */
    @FXML
    public void handleBackBtn(){
        String fxmlPath ="../fxmls/mode.fxml";
        String stageTitle = "Mode";
        String fileException ="Mode";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }



}
