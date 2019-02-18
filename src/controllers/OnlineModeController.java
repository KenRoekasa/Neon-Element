package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OnlineModeController implements Initializable{
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    @FXML
    public void handleHostGame(ActionEvent actionEvent){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/fxml/online_setup.fxml"));
        try {
            Pane root = loader.load();
            OnlineSetUpController onlineSetUpController = loader.getController();
            onlineSetUpController.setStage(stage);
            stage.getScene().setRoot(root);
            stage.setTitle("Online Mode Configuration");

        } catch (IOException e) {
            System.out.println("crush in loading online setup board ");
            e.printStackTrace();
        }

    }

    @FXML
    public void handleJoinGame(ActionEvent event){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/fxml/ip_join.fxml"));

        try {
            Pane root = loader.load();
            JoinController controller  = loader.getController();
            controller.setStage(stage);
            stage.getScene().setRoot(root);
            stage.setTitle("Join a Game");


        } catch (IOException e) {
            System.out.println("crush in loading join board ");
            e.printStackTrace();
        }
    }


    @FXML
    public void handleBackBtn(ActionEvent actionEvent){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/fxml/mode_board.fxml"));

        try {
            Pane root = loader.load();
            stage.getScene().setRoot(root);
            ModeController controller = loader.getController();
            controller.setStage(stage);
            stage.setTitle("Mode");
        } catch (IOException e) {
            System.out.println("crush in loading mode sboard ");
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
