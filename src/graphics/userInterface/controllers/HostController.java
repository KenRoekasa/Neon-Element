package graphics.userInterface.controllers;

import client.GameClient;

import client.GameStateGenerator;
import client.ClientGameState;
import server.GameServer;
import server.ServerGameState;
import server.ServerGameStateGenerator;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

//For local_setup scene
public class HostController extends UIController{
    @FXML
    Label back,start;
    private ClientGameState gameState;
    private ServerGameState serverState;

    // directly go to local mode map
    @FXML
    public void handleStartBtn(ActionEvent event){
        // create game rules
        // todo make this configurable
        gameState = GameStateGenerator.createEmptyState();
        serverState = ServerGameStateGenerator.createEmptyState();
        try {
            // Create server
            GameServer server = new GameServer(serverState);
            server.start();

            String addr = "localhost";
            GameClient gameBoard = new GameClient(stage, gameState, addr);
            Scene scene = gameBoard.getScene();
            gameBoard.startNetwork();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void handleBackBtn(ActionEvent event){
        String fxmlPath ="../fxmls/online_setup.fxml";
        String stageTitle ="Online configuration";
        String fileException ="Online Setup";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        back.setTextFill(outline);
        start.setTextFill(outline);
        back.setEffect(blend);
        start.setEffect(blend);
    }
}

