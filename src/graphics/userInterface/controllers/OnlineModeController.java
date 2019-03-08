package graphics.userInterface.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


import java.net.URL;
import java.util.ResourceBundle;

public class OnlineModeController extends UIController{
    @FXML
    Label host,join,back;

    @FXML
    public void handleHostGame(ActionEvent actionEvent){
        String fxmlPath = "../fxmls/online_setup.fxml";
        String stageTitle = "Online Mode Configuration";
        String fileException ="Online Setup";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }

    @FXML
    public void handleJoinGame(ActionEvent event) {
        String fxmlPath = "../fxmls/ip_join.fxml";
        String stageTitle = "Join a game";
        String fileException = "IP Join ";
        FxmlLoader loader = new FxmlLoader(fxmlPath, stage, stageTitle, fileException, audioManager);
    }


    @FXML
    public void handleBackBtn(ActionEvent actionEvent){
        String fxmlPath ="../fxmls/mode.fxml";
        String stageTitle = "Mode";
        String fileException ="Mode";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }



}
