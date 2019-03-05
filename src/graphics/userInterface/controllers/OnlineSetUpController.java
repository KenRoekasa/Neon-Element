package graphics.userInterface.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

import java.net.URL;
import java.util.ResourceBundle;

public class OnlineSetUpController extends UIController implements Initializable{

    public RadioButton easy, normal, hard, single, multiple, onePlayer, twoPlayer, threePlayer;
    public Button back_btn;
    public Button create_btn;
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
        //removeRadioCircle();
    }

    @SuppressWarnings("Duplicates")
    private void removeRadioCircle() {
        // there might be a less long way of doing this
        easy.getStyleClass().remove("radio-button");
        easy.getStyleClass().add("button");

        normal.getStyleClass().remove("radio-button");
        normal.getStyleClass().add("button");

        hard.getStyleClass().remove("radio-button");
        hard.getStyleClass().add("button");

        single.getStyleClass().remove("radio-button");
        single.getStyleClass().add("button");

        multiple.getStyleClass().remove("radio-button");
        multiple.getStyleClass().add("button");

        onePlayer.getStyleClass().remove("radio-button");
        onePlayer.getStyleClass().add("button");

        twoPlayer.getStyleClass().remove("radio-button");
        twoPlayer.getStyleClass().add("button");

        threePlayer.getStyleClass().remove("radio-button");
        threePlayer.getStyleClass().add("button");

    }
}
