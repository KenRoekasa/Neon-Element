package controllers;

import javafx.event.ActionEvent;
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

public class OnlineModeController implements Initializable{
    private Stage stage;
    private Rectangle2D stageSize;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setStageSize(Rectangle2D stageSize) {
        this.stageSize = stageSize;
    }

    @FXML
    public void handleHostGame(ActionEvent actionEvent){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/online_setup.fxml"));
        try {
            Pane root = loader.load();
            Scene scene = new Scene(root,stageSize.getWidth(),stageSize.getHeight());
            OnlineSetUpController onlineSetUpController = loader.getController();
            onlineSetUpController.setStage(stage);
            onlineSetUpController.setStageSize(stageSize);
            stage.setTitle("Online Mode Configuration");
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException e) {
            System.out.println("crush in loading online setup board ");
            e.printStackTrace();
        }

    }

    @FXML
    public void handleJoinGame(ActionEvent actionEvent){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/join_ip.fxml"));

        try {
            Pane root = loader.load();
            Scene scene =new Scene(root,stageSize.getWidth(),stageSize.getHeight());
            stage.setTitle("Join a Game");
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException e) {
            System.out.println("crush in loading join board ");
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
