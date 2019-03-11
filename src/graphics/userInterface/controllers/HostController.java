package graphics.userInterface.controllers;

import client.ClientGameState;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class HostController extends UIController{
    private ClientGameState gameState;
    private String iP;
    //TODO: call this function in the networking part
    public void setiP(String iP) {
        this.iP = iP;
        ip_value.set(iP);
    }

    @FXML
    Text ip_address;
    StringProperty ip_value = new SimpleStringProperty();

    public void handleStartBtn(ActionEvent event){
        String fxmlPath ="../fxmls/lobby.fxml";
        String stageTitle ="Game Lobby";
        String fileException ="lobby";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);

      /*  // create game rules
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
*/
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

