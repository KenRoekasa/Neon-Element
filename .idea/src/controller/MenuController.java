package Controller;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
/*Click play to jump to the game board*/
public class PlayController {
    private Scene board;
    public Scene getBoard() {
        return board;
    }

    public void setBoard(Scene board) {
        this.board = board;
    }

    public void openSecondScene(ActionEvent actionEvent) {
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(board);
    }
}
