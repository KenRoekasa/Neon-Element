package graphics.userInterface.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OnlineModeController extends UIController{

    @FXML
    public void handleHostGame(ActionEvent actionEvent){
        String fxmlPath = "../fxmls/online_setup.fxml";
        String stageTitle = "Online Mode Configuration";
        String fileException ="Online Setup";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException);
    }

    @FXML
    public void handleJoinGame(ActionEvent event) {
        String fxmlPath = "../fxmls/ip_join.fxml";
        String stageTitle = "Join a game";
        String fileException = "IP Join ";
        FxmlLoader loader = new FxmlLoader(fxmlPath, stage, stageTitle, fileException);
    }


    @FXML
    public void handleBackBtn(ActionEvent actionEvent){
        String fxmlPath ="../fxmls/mode_board.fxml";
        String stageTitle = "Mode";
        String fileException ="Mode";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException);
    }

}
