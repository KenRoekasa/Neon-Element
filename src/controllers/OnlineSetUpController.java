package controllers;

import client.ClientBoard;
import client.GameState;
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
    private GameState gameState;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setStageSize(Rectangle2D stageSize) {
        this.stageSize = stageSize;
    }

    @FXML
    public void handleCreateBtn(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/ip_host.fxml"));
        try {
            Pane root = loader.load();
            stage.getScene().setRoot(root);
            LoadingController controller = new LoadingController();
            controller.setStage(stage);
            stage.setTitle("Game IP");

        } catch (IOException e) {
            System.out.println("crush in loading host ip board ");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBackBtn(){
        //select mode
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/mode_board.fxml"));
        try {
            Pane root = loader.load();
            stage.getScene().setRoot(root);
            ModeController modeController = loader.getController();
            modeController.setStage(stage);
            modeController.setStageSize(stageSize);
            stage.setTitle("Mode");
        } catch (IOException e) {
            System.out.println("crush in loading mode board ");
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
