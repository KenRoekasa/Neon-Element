package graphics.userInterface.controllers;

import client.GameClient;
import client.ClientGameState;
import client.GameStateGenerator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

//For local_setup scene
public class JoinController {
    private ClientGameState gameState;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void handleOkBtn(){
        // create game rules
        // todo make this configurable
            gameState = GameStateGenerator.createDemoGamestate();
        //g.getPlayer().getHealth();
        try {
            boolean networked = true;
            GameClient gameBoard = new GameClient(stage, gameState, networked);
            Scene scene = gameBoard.getScene();
            gameBoard.startNetwork();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void handleBackBtn(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/online_mode.fxml"));

        try {
            Pane root = loader.load();
            stage.getScene().setRoot(root);
            OnlineModeController controller = loader.getController();
            controller.setStage(stage);
            stage.setTitle("Online Mode");

        } catch (IOException e) {
            System.out.println("crush in loading online mode board ");
            e.printStackTrace();
        }
    }

}

