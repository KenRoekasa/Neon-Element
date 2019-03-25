package graphics.userInterface.controllers;


import engine.model.ScoreBoard;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.ArrayList;

/**
 * Controller for the leaderboard when game over
 */
public class LeaderboardController extends UIController {
    /**
     * Players' ids shown on GUI
     */
    @FXML
    Text player1_id,player2_id,player3_id,player4_id;
    /**
     * Players' kills shown on GUI
     */
    @FXML
    Text player1_kills,player2_kills,player3_kills,player4_kills;
    /**
     * Players deaths shown on GUI
     */
    @FXML
    Text player1_deaths,player2_deaths,player3_deaths,player4_deaths;
    /**
     * Players score shown on GUI
     */
    @FXML
    Text player1_score,player2_score,player3_score,player4_score;
    /**
     * The section of rank 3th and 4 th pane
     */
    @FXML
    GridPane rank_3,rank_4;
    /**
     * The highest score's player's id
     */
    private int player1;
    /**
     * The second highest score's player's id
     */
    private int player2;
    /**
     * The third highest score's player's id
     */
    private int player3;
    /**
     * The lowest score's player's id
     */
    private int player4;

    /**
     * The scoreboard of the game
     */
    private ScoreBoard scoreBoard;


    /**
     * The array list of the leaderboard information
     */
    private ArrayList<Integer> leaderBoard ;
    /**
     * Number of total players
     */
    int num_players;

    /**
     * Set the scoreboard of the game
     * @param scoreBoard game's scoreboard
     */
    public void setScoreBoard(ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    /**
     * Set the number of total players of the game
     * @param num_players total players of the game
     */
    public void setNum_players(int num_players) {
        this.num_players = num_players;
    }

    /**
     * Set the leaderboard of the game
     * @param leaderBoard the leaderboard of the game
     */
    public void setLeaderBoard(ArrayList<Integer> leaderBoard) {
        this.leaderBoard = leaderBoard;
    }


    /**
     * Show the leaderboard on GUI
     */
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

    /**
     * Show the information of a single players
     * @param id player's id
     * @param player_id player's id text on GUI
     * @param death player's death text on GUI
     * @param kills player's kills text on GUI
     * @param score player's score text on GUI
     */

    public void showInfo(int id,Text player_id,Text death,Text kills,Text score){
        player_id.setText(String.valueOf(id));
        death.setText(scoreBoard.getPlayerDeaths(id).toString());
        kills.setText(scoreBoard.getPlayerKills(id).toString());
        score.setText(scoreBoard.getPlayerScore(id).toString());
    }

    /**
     * Handle the action when press the quit button which will go back to the initial menu page
     */
    @FXML
    public void handleQuitBtn(){
        String fxmlPath = "../fxmls/menu.fxml";
        String stageTitle = "Mode";
        String fileException ="Mode";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }

    /**
     * Handle the action when press the play again button which will direct to the mode selection page
     */
    @FXML
    public void handlePlayAgainBtn(){
        String fxmlPath = "../fxmls/mode.fxml";
        String stageTitle = "Mode";
        String fileException ="Mode";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }

}
