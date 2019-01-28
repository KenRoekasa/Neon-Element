package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/* Menu buttons:
1. Play -> Board
2. Options
3. Help
4. Exit
*/

public class MenuController implements Initializable{
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void handleBTNPlay(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../userInterface/game_board.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Board Screen");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBTNOptions(ActionEvent actionEvent){}

    @FXML
    public void handleBTNHelp(ActionEvent actionEvent){}

    @FXML
    public void handleBTNExit(ActionEvent actionEvent){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
