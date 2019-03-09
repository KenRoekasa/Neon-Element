package graphics.userInterface.controllers;

import client.GameClient;

import client.ClientGameState;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

//For local_setup scene
public class HostController extends UIController{
    private ClientGameState gameState;

    // directly go to local mode map
    @FXML
    Text ip_address;
    StringProperty ip_value = new SimpleStringProperty();

    public void handleStartBtn(ActionEvent event){
        // create game rules
        // todo make this configurable
            //gameState = GameStateGenerator.createDemoGamestate();
        //g.getPlayer().getHealth();
        try {
            boolean networked = false;
            GameClient gameBoard = new GameClient(stage, gameState, networked, audioManager);
            //Scene scene = gameBoard.getScene();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void handleBackBtn(ActionEvent event){
        String fxmlPath ="../fxmls/online_setup.fxml";
        String stageTitle ="Online configuration";
        String fileException ="Online Setup";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }

    //TODO: load the ip address here
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ip_address.textProperty().bind(ip_value);
    }
    
}

