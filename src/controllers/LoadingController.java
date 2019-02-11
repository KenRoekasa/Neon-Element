package controllers;

import client.ClientBoard;
import client.GameState;
import client.GameStateGenerator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoadingController implements Initializable {
    private GameState gameState;
    private Stage stage;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void handleLoading(){

        // create game rules
        // todo make this configurable
        gameState = GameStateGenerator.createDemoGamestate();
        //g.getPlayer().getHealth();
        try {
            ClientBoard gameBoard = new ClientBoard(stage, gameState);
            Scene scene = gameBoard.getScene();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
