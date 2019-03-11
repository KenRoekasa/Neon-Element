package graphics.userInterface.controllers;

import client.GameClient;
import client.ClientGameState;
import client.GameStateGenerator;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;


public class JoinController extends UIController {
    private ClientGameState gameState;
    public TextField ip;

    @FXML
    public void handleStartBtn(){
        // create game rules
        // todo make this configurable
            gameState = GameStateGenerator.createDemoGamestate();
        //g.getPlayer().getHealth();
        try {
            String addr = ip.getText();
            GameClient gameBoard = new GameClient(stage, gameState, addr, audioManager);
            Scene scene = gameBoard.getScene();
            gameBoard.startNetwork();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void handleBackBtn(){
        String fxmlPath ="../fxmls/online_mode.fxml";
        String stageTitle ="Online Mode";
        String fileException ="Online Mode";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }



}

