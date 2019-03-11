package graphics.userInterface.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/*Mode board: online/local*/
public class ModeController extends UIController{
    @FXML
    Label online,local,back;
   //local -> local_setup
    @FXML
    public void handleLocalBtn(ActionEvent actionEvent){
        String fxmlPath = "../fxmls/local_setup.fxml";
        String stageTitle = "Local Mode Configuration";
        String fileException ="Local Setup";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }

    //online->choose host/ join
    @FXML
    public void handleOnlineBtn(ActionEvent actionEvent){
        String fxmlPath ="../fxmls/online_mode.fxml";
        String stageTitle ="Online Mode Selection" ;
        String fileException ="Online Mode";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }

    //back->mode board
    @FXML
    public void handleBackBtn(ActionEvent actionEvent){
        String fxmlPath ="../fxmls/menu.fxml";
        String stageTitle ="Menu" ;
        String fileException ="Menu";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }

}
