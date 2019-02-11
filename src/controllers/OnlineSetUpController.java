package controllers;

import client.ClientBoard;
import client.ClientGameState;
import client.GameStateGenerator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OnlineSetUpController implements Initializable{
    private Stage stage;
    private Rectangle2D stageSize;
    private ClientGameState gameState;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setStageSize(Rectangle2D stageSize) {
        this.stageSize = stageSize;
    }

    @FXML
    public void handleCreateBtn(){
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

    @FXML
    public void handleBackBtn(){
        //select mode
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/mode_board.fxml"));
        try {
            Pane root = loader.load();
            Scene scene =new Scene(root,stageSize.getWidth(),stageSize.getHeight());
            ModeController modeController = loader.getController();
            modeController.setStage(stage);
            modeController.setStageSize(stageSize);
            stage.setTitle("Mode");
            stage.setScene(scene);
            stage.setFullScreen(true); //setFullScreen must set after setting scene
            stage.show();
        } catch (IOException e) {
            System.out.println("crush in loading mode board ");
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
