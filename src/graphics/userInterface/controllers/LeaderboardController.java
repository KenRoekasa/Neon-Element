package graphics.userInterface.controllers;

import engine.ScoreBoard;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;


public class LeaderboardController extends UIController {
    @FXML
    Label player1_id,player2_id,player3_id,player4_id;
    @FXML
    Label player1_kills,player2_kills,player3_kills,player4_kills;
    @FXML
    Label player1_deaths,player2_deaths,player3_deaths,player4_deaths;
    @FXML
    Label player1_score,player2_score,player3_score,player4_score;
    @FXML
    GridPane rank_3,rank_4;

    int player1;
    int player2;
    int player3;
    int player4;

    private ScoreBoard scoreBoard;



    private ArrayList<Integer> leaderBoard ;
    int num_players;

    public void setScoreBoard(ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    public void setNum_players(int num_players) {
        this.num_players = num_players;
    }
    public void setLeaderBoard(ArrayList<Integer> leaderBoard) {
        this.leaderBoard = leaderBoard;
    }

    public void showLeaderBoard() {
        player1=leaderBoard.get(0);
        player2=leaderBoard.get(1);
        showInfo(player1,player1_id,player1_deaths,player1_kills,player1_score);
        showInfo(player2,player2_id,player2_deaths,player2_kills,player2_score);

        if (num_players == 2) {
            rank_3.setVisible(false);
            rank_4.setVisible(false);
        } else if (num_players == 3) {
            player3 = leaderBoard.get(2);
            showInfo(player3,player3_id,player3_deaths,player3_kills,player3_score);
            rank_4.setVisible(false);
        }else if (num_players==4){
            player3 = leaderBoard.get(2);
            player4 = leaderBoard.get(3);
            showInfo(player3,player3_id,player3_deaths,player3_kills,player3_score);
            showInfo(player4,player4_id,player4_deaths,player4_kills,player4_score);
        }
    }

    public void showInfo(int id,Label player_id,Label death,Label kills,Label score){
        player_id.setText(String.valueOf(id));
        death.setText(scoreBoard.getPlayerDeaths(id).toString());
        kills.setText(scoreBoard.getPlayerKills(id).toString());
        score.setText(scoreBoard.getPlayerScore(id).toString());
    }
    @FXML
    public void handleQuitBtn(){
        String fxmlPath = "../fxmls/menu.fxml";
        String stageTitle = "Mode";
        String fileException ="Mode";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }

    @FXML
    public void handlePlayAgainBtn(){
        String fxmlPath = "../fxmls/mode.fxml";
        String stageTitle = "Mode";
        String fileException ="Mode";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }


}
