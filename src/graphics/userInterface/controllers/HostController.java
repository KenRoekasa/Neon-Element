package graphics.userInterface.controllers;

import client.ClientBoard;

import client.ClientGameState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;

//For local_setup scene
public class HostController {
    private ClientGameState gameState;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // directly go to local mode map
    @FXML
    public void handleStartBtn(ActionEvent event){
        // create game rules
        // todo make this configurable
            //gameState = GameStateGenerator.createDemoGamestate();
        //g.getPlayer().getHealth();
        try {
            ClientBoard gameBoard = new ClientBoard(stage, gameState);
            Scene scene = gameBoard.getScene();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void handleBackBtn(ActionEvent event){
<<<<<<< HEAD:src/controllers/HostController.java
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/fxml/online_setup.fxml"));
=======
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/online_setup.fxml"));
>>>>>>> 57ad7f93eb51328661503183acca5c860c7e0fd2:src/graphics/userInterface/controllers/HostController.java

        try {
            Pane root = loader.load();
            stage.getScene().setRoot(root);
            OnlineSetUpController controller = loader.getController();
            controller.setStage(stage);
            stage.setTitle("Online Setup");

        } catch (IOException e) {
            System.out.println("crush in loading online setup board ");
            e.printStackTrace();
        }
    }

}

