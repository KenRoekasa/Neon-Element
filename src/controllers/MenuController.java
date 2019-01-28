package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import userInterface.GameBoard;

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
            GameBoard gameBoard = new GameBoard(stage);
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
