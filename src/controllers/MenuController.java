package controllers;

import client.ClientBoard;
import client.GameState;
import client.GameStateGenerator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/* Menu buttons:
1. Play
2. Options
3. Help
4. Exit
*/

public class MenuController implements Initializable{
    private Stage stage;
    private GameState gameState;

    private Rectangle2D stageSize;
    @FXML
    private Text health;
    @FXML
    private Text speed;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setStageSize(Rectangle2D stageSize) {
        this.stageSize = stageSize;
    }

    // play -> mode selection
    @FXML
    public void handleBTNPlay(ActionEvent actionEvent) {

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


    @FXML
    public void handleBTNOptions(ActionEvent actionEvent){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../userInterface/option_board.fxml"));
        try {
            Parent root = (Parent)fxmlLoader.load();
            stage.getScene().setRoot(root);

            stage.setTitle("Options");
            stage.show();
        } catch (IOException e) {
            System.out.println("crush in loading option board ");
            e.printStackTrace();
        }

    }

    @FXML
    public void handleBTNHelp(ActionEvent actionEvent){

    }

    @FXML
    public void handleBTNExit(ActionEvent actionEvent){
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
