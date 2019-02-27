package graphics.userInterface.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class OnlineSetUpController extends UIController{

    @FXML
    Label create,back;
    @FXML
    public void handleCreateBtn(){
        String fxmlPath = "../fxmls/ip_host.fxml";
        String stageTitle ="Host a Game" ;
        String fileException ="IP Host";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException);
    }

    @FXML
    public void handleBackBtn(){
        //select mode
        String fxmlPath = "../fxmls/online_mode.fxml";
        String stageTitle = "Online Configuration";
        String fileException ="Online Setup";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        create.setTextFill(outline);
        back.setTextFill(outline);
        create.setEffect(blend);
        back.setEffect(blend);

    }


}
