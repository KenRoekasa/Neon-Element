package graphics.userInterface.controllers;

import client.ClientGameState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
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

    @FXML
    public void handleCreateBtn(){
<<<<<<< HEAD:src/controllers/OnlineSetUpController.java
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/fxml/ip_host.fxml"));
=======
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/ip_host.fxml"));
>>>>>>> 57ad7f93eb51328661503183acca5c860c7e0fd2:src/graphics/userInterface/controllers/OnlineSetUpController.java
        try {
            Pane root = loader.load();
            stage.getScene().setRoot(root);
            HostController controller = loader.getController();
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
<<<<<<< HEAD:src/controllers/OnlineSetUpController.java
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/fxml/mode_board.fxml"));
=======
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/mode_board.fxml"));
>>>>>>> 57ad7f93eb51328661503183acca5c860c7e0fd2:src/graphics/userInterface/controllers/OnlineSetUpController.java
        try {
            Pane root = loader.load();
            stage.getScene().setRoot(root);
            ModeController modeController = loader.getController();
            modeController.setStage(stage);
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
