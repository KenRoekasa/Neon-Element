package graphics.userInterface.controllers;

import engine.GameState;
import engine.ScoreBoard;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Controller for gameover.fxml
 */
public class GameOverController extends UIController{
    @FXML
    Label back;
    private ScoreBoard scoreBoard;

    public void setScoreBoard(ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
    }


    /**
     * Handle the action of pressing back button which will go back to menu.fxml
     */
    public void handleQuitBtn() {
        String fxmlPath = "../fxmls/menu.fxml";
        String stageTitle = "Menu";
        String fileException ="Menu";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }

    public void jumpToLeaderboard(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/leaderboard.fxml"));
        try {
            Pane root = loader.load();
            stage.getScene().setRoot(root);
            stage.setTitle("Leader Board");
            LeaderboardController controller = loader.getController();
            controller.setStage(stage);
            controller.setAudioManager(audioManager);
            controller.setScoreBoard(scoreBoard);
            controller.setLeaderBoard(scoreBoard.getLeaderBoard());
            controller.setNum_players(scoreBoard.getLeaderBoard().size());
            controller.showLeaderBoard();
        } catch (IOException e) {
            System.out.println("crush in loading leader board ");
            e.printStackTrace();
        }

    }
}
