package graphics.userInterface.controllers;


import client.ClientGameState;
import engine.model.ScoreBoard;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.scene.paint.Color.*;

/**
 * Controller of hud.fxml which mainly controls the properties of the game state
 */
//To get players' health and speed in top-left hud
public class HUDController extends UIController implements Initializable {
    /**
     * Mode of current game
     */
    private String mode;

    /**
     * Kills needed to win
     */
    private int kill_left;
    /**
     * Time left to end the game
     */
    private int time;


    /**
     * The client game state
     */
    private ClientGameState gameState;
    /**
     * The score board of current game
     */
    private ScoreBoard scoreBoard;
    /**
     * Client player id
     */
    private int playerId;
    /**
     * The leader board of current game
     */
    private ArrayList<Integer> leaderBoard;

    /**
     * Output string template of leader board
     */
    private final static String TEMP = "Player %s with %s ";

    /** Set the client player id
     * @param playerId client player id
     */
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    /** Set the score board
     * @param scoreBoard current score board
     */
    public void setScoreBoard(ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    /** Set the leader board
     * @param leaderBoard current leader board information
     */
    public void setLeaderBoard(ArrayList<Integer> leaderBoard) {
        this.leaderBoard = leaderBoard;
    }

    /** Set the current game state
     * @param gameState current game state
     */
    public void setGameState(ClientGameState gameState) {
        this.gameState = gameState;
    }


    /**
     * The progress bar to show the health left
     */
    @FXML
    private ProgressBar health_bar;


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
    @FXML Text death;
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
     * Number of the total players in the game
     */
    private int num_player;

    /** Set the total players number of the game
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
    }


    /** Initialise the controller setting, implement the user interface value binding
     * @param location  url location
     * @param resources resource bundled
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {


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
     * Update the all string properties in this controller when every call this function
     */
    public void update() {

        if (gameState.getPlayer().getHealth() < 0) {
            health_bar.setProgress(0);
            health_bar.progressProperty().setValue(0);

        } else {
            // progress range is 0-1
            health_bar.setProgress(gameState.getPlayer().getHealth()/100);

        }

        totalKills.set(String.valueOf((int) (gameState.getScoreBoard().getPlayerKills(playerId))));

        deathTimes.set(String.valueOf((int)(gameState.getScoreBoard().getPlayerDeaths(playerId))));

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
}

