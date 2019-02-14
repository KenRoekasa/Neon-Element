package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
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

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // play -> mode selection
    @FXML
    public void handleBTNPlay(ActionEvent actionEvent) {

      //select mode
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/mode_board.fxml"));

        try {
            Pane root = loader.load();
            stage.getScene().setRoot(root);
            ModeController controller = loader.getController();
            controller.setStage(stage);
            stage.setTitle("Mode");

        } catch (IOException e) {
            System.out.println("crush in loading mode board ");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBTNOptions(ActionEvent actionEvent){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../userInterface/setting.fxml"));
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
