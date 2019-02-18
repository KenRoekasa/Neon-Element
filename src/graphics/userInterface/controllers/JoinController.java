package graphics.userInterface.controllers;

import client.ClientBoard;
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
            ClientBoard gameBoard = new ClientBoard(stage, gameState);
            Scene scene = gameBoard.getScene();
            gameBoard.startGame();
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    @FXML
    public void handleBackBtn(){
<<<<<<< HEAD:src/controllers/JoinController.java
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/fxml/online_mode.fxml"));
=======
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/online_mode.fxml"));
>>>>>>> 57ad7f93eb51328661503183acca5c860c7e0fd2:src/graphics/userInterface/controllers/JoinController.java

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

