package graphics.userInterface.controllers;


import client.ClientGameState;
import client.GameClient;
import engine.entities.CooldownItems;
import engine.entities.CooldownValues;
import engine.model.GameType;
import engine.model.ScoreBoard;
import engine.model.gametypes.FirstToXKillsGame;
import engine.model.gametypes.HillGame;
import engine.model.gametypes.Regicide;
import engine.model.gametypes.TimedGame;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller of hud.fxml which mainly controls the properties of the game state
 */
//To get players' health and speed in top-left hud
public class HUDController extends UIController implements Initializable {


    /**
     * Mode of current game
     */
    private GameType.Type mode;

    /**
     * Kills needed to win
     */
    private int kill_left;
    /**
     * Time left to end the game
     */
    private int time;

    /**
     * Progress bar of score left to end the game
     */
    @FXML
    private ProgressBar score_bar;
    /**
     * The client game state
     */
    private ClientGameState gameState;
    /**
     * The score board of current game
     */
    private ScoreBoard scoreBoard;
    /**
     * Client ƒplayer id
     */
    private int playerId;
    /**
     * The leader board of current game
     */
    private ArrayList<Integer> leaderBoard;

    /**
     * For time mode game
     */
    private int time_duration;
    /**
     * Output string template of leader board
     */
    private final static String TEMP = "Player %s with %s ";

    /**
     * Set the client player id
     *
     * @param playerId client player id
     */
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    /**
     * Set the score board
     *
     * @param scoreBoard current score board
     */
    public void setScoreBoard(ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    /**
     * Set the leader board
     *
     * @param leaderBoard current leader board information
     */
    public void setLeaderBoard(ArrayList<Integer> leaderBoard) {
        this.leaderBoard = leaderBoard;
    }


    public void setMode(GameType.Type mode) {
        this.mode = mode;
        switch (mode) {
            case FirstToXKills:
                score_board.setVisible(true);
                time_board.setVisible(false);
                break;
            case Timed:
                time_duration =(int) TimedGame.getDuration();
                score_board.setVisible(false);
                time_board.setVisible(true);
                break;
            case Regicide:
                score_board.setVisible(true);
                time_board.setVisible(false);
                break;
            case Hill:
                score_board.setVisible(true);
                time_board.setVisible(false);
        }
    }


    /**
     * The progress bar to show the health left
     */
    @FXML
    private ProgressBar health_bar;

    @FXML
    private ProgressIndicator timer;

    /**
     * The text of number of kills shown on the player's information section
     */
    @FXML
    private Text kills;
    /**
     * The string property of number of kills on the player's information section
     */
    private StringProperty totalKills;

    /**
     * The text of death times shown on the player's information section
     */
    @FXML
    Text death;
    /**
     * The string property of the death times
     */
    private StringProperty deathTimes;

    /**
     * The labels of players shown on the leader board section
     */
    @FXML
    private Label player1, player2, player3, player4;
    /**
     * The string properties of players's leading information
     */
    private StringProperty player1Property, player2Property, player3Property, player4Property;

    /**
     * The cooldown progress
     */
    @FXML
    private ProgressIndicator lightCD,heavyCD,changeCD;


    /**
     * The icons of the skills
     */
    @FXML
    private StackPane lightIcon, heavyIcon,changeIcon;

    /**
     * Number of the total players in the game
     */
    private int num_player;

    /**
     * Score board pane
     */
    @FXML
    private AnchorPane score_board;

    /**
     * Bottom Hbox which includes timeboard and cool down progress indicators
     * */
    @FXML
    private HBox time_board;

    /**
     * Health value property
     */
    private SimpleDoubleProperty healthProperty;
    /**
     * Score value property
     */
    private SimpleDoubleProperty scoreProperty;
    /**
     * Time progress property
     */
    private SimpleDoubleProperty timeProperty;


    /**
     * Set the total players number of the game
     *
     * @param num_player
     */
    public void setNum_player(int num_player) {
        this.num_player = num_player;
    }

    /**
     * Constructor for initialising the string property's fields;
     */
    public HUDController() {
        totalKills = new SimpleStringProperty();
        deathTimes = new SimpleStringProperty();
        player1Property = new SimpleStringProperty();
        player2Property = new SimpleStringProperty();
        player3Property = new SimpleStringProperty();
        player4Property = new SimpleStringProperty();
        healthProperty = new SimpleDoubleProperty();
        scoreProperty = new SimpleDoubleProperty();
        timeProperty = new SimpleDoubleProperty();
    }

    /**
     * Set the game state
     * @param gameState current game state
     */
    public void setGameState(ClientGameState gameState) {
        this.gameState = gameState;
        this.num_player = gameState.getScoreBoard().getLeaderBoard().size();
    }


    /**
     * Initialise the controller setting, implement the user interface value binding
     *
     * @param location  url location
     * @param resources resource bundled
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {


        health_bar.progressProperty().bind(healthProperty);
        score_bar.progressProperty().bind(scoreProperty);
        timer.progressProperty().bind(timeProperty);

        kills.textProperty().bind(totalKills);
        death.textProperty().bind(deathTimes);

        player1.textProperty().bind(player1Property);
        player2.textProperty().bind(player2Property);
        player3.textProperty().bind(player3Property);
        player4.textProperty().bind(player4Property);

        switch (num_player) {
            case 2: {
                player3.setVisible(false);
                player4.setVisible(false);
                break;
            }

            case 3: {
                player3.setVisible(true);
                player4.setVisible(false);
                break;
            }
            case 4: {
                player3.setVisible(true);
                player4.setVisible(true);
            }
        }
    }

    /**
     * Update all the information in HUD
     */
    public void update() {
        updateUserinfo();
        updateLeaderboard();
        switch (mode){
                case FirstToXKills:
                    updateScore(FirstToXKillsGame.getKillsNeeded());
                    break;
                case Timed:
                    updateTime(time_duration);
                    break;
                case Regicide:
                    updateScore(((Regicide)(gameState.getGameType())).getScoreNeeded());
                    break;
                case Hill:
                    updateScore(((HillGame) gameState.getGameType()).getScoreNeeded());
        }
    }

    /**
     * Update the user's information on the top-right corner and cool down progress on the bottom-left corner
     */

    public void updateUserinfo() {
        if (gameState.getPlayer().getHealth() < 0) {
            healthProperty.setValue(0);

        } else {
            // progress range is 0-1
            healthProperty.setValue(gameState.getPlayer().getHealth() / 100);
        }

        // Cooldown check
        long l = GameClient.timeElapsed - gameState.getPlayer().getLastUsed(CooldownItems.LIGHT);
        float v = l / (CooldownValues.lightAttackCD * 1000);

        if(v < 1){ // is not 100 percent
            lightIcon.setVisible(false);
        }else if(v>=1){
            lightIcon.setVisible(true);
        }
        lightCD.setProgress(v);
        l = GameClient.timeElapsed - gameState.getPlayer().getLastUsed(CooldownItems.HEAVY);
        v = l / (CooldownValues.heavyAttackCD * 1000);


        if(v < 1){ // is not 100 percent
            heavyIcon.setVisible(false);
        }else if(v>=1){
            heavyIcon.setVisible(true);
        }

        heavyCD.setProgress(v);

        l = GameClient.timeElapsed - gameState.getPlayer().getLastUsed(CooldownItems.CHANGESTATE);
        v = l / (CooldownValues.changeStateCD * 1000);

        if(v < 1){ // is not 100 percent
            changeIcon.setVisible(false);
        }else if(v>=1){
            changeIcon.setVisible(true);
        }

        l = GameClient.timeElapsed - gameState.getPlayer().getLastUsed(CooldownItems.CHANGESTATE);
        v = l / (CooldownValues.changeStateCD * 1000);
        changeCD.setProgress(v);


        totalKills.set(String.valueOf((int) (gameState.getScoreBoard().getPlayerKills(playerId))));
        deathTimes.set(String.valueOf((int) (gameState.getScoreBoard().getPlayerDeaths(playerId))));

    }
    /**
     * Update the leaderboard
     */
    public void updateLeaderboard() {
        String s1 = String.format(TEMP, leaderBoard.get(0).toString(), scoreBoard.getPlayerScore(leaderBoard.get(0)));
        String s2 = String.format(TEMP, leaderBoard.get(1).toString(), scoreBoard.getPlayerScore(leaderBoard.get(1)));
        player1Property.set(s1);
        player2Property.set(s2);
        if (num_player == 3) {
            String s3 = String.format(TEMP, leaderBoard.get(2).toString(), scoreBoard.getPlayerScore(leaderBoard.get(2)));
            player3Property.set(s3);
        } else if (num_player == 4) {
            String s3 = String.format(TEMP, leaderBoard.get(2).toString(), scoreBoard.getPlayerScore(leaderBoard.get(2)));
            String s4 = String.format(TEMP, leaderBoard.get(3).toString(), scoreBoard.getPlayerScore(leaderBoard.get(3)));
            player3Property.set(s3);
            player4Property.set(s4);
        }
    }

    /**
     * Update the score progress bar
     */
    public void updateScore(int score) {
        double score_player = scoreBoard.getPlayerScore(playerId);
        scoreProperty.setValue(score_player/score);
    }

    /**
     * Update the time left
     */
    public void updateTime(long time){
        long elapsedTime = GameClient.timeElapsed;
        timeProperty.setValue((double)elapsedTime/time);
    }



}

