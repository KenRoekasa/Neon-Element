package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

/* Menu buttons:
1. Play -> Board
2. Options
3. Help
4. Exit
*/

public class MenuController implements Initializable{
    private Scene board;
    public Scene getBoard() {
        return board;
    }

    public void setBoard(Scene board) {
        this.board = board;
    }

    @FXML
    public void handleBTNPlay(ActionEvent actionEvent) {
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(board);
    }

    @FXML
    public void handleBTNOptions(ActionEvent actionEvent){}

    @FXML
    public void handleBTNHelp(ActionEvent actionEvent){}

    @FXML
    public void handleBTNExigt(ActionEvent actionEvent){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
