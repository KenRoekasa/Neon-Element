package graphics.userInterface.controllers;

import client.GameClient;

import client.ClientGameState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

//For local_setup scene
public class HostController extends UIController{
    @FXML
    Label back,start;
    private ClientGameState gameState;

    // directly go to local mode map
    @FXML
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

    
}

