package graphics.userInterface.controllers;


import client.ClientGameState;
import engine.model.ScoreBoard;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for the leaderboard
 */
public class LeaderboardController extends UIController {
    /**
     * The size of stage
     */
    private Rectangle stageSize;
    /**
     * The pane of hud
     */
    private Pane hudPane;
    /**
     * The node pane
     */
    private Pane node;
    /**
     * Current game state
     */
    private ClientGameState gameState;

    /**
     * Set the node
     *
     * @param node subnode of scene
     */
    public void setNode(Pane node) {
        this.node = node;
    }

    /**
     * Set size of the current stage
     *
     * @param stageSize the size of the current stage
     */
    public void setStageSize(Rectangle stageSize) {
        this.stageSize = stageSize;
    }

    /**
     * Set hud pane
     *
     * @param hudPane hud pane of game
     */
    public void setHudPane(Pane hudPane) {
        this.hudPane = hudPane;
    }

    /**
     * Set the stage of current scene
     *
     * @param stage     the stage of current game
     * @param gameState the current game state
     */
    public void setStage(Stage stage, ClientGameState gameState) {
        this.gameState = gameState;
        super.stage = stage;
    }

    /**
     * Players' ids shown on GUI
     */
    @FXML
    Text player1_id, player2_id, player3_id, player4_id;
    StringProperty player1IdProperty, player2IdProperty, player3IdProperty, player4IdProperty;

    /**
     * Players' kills shown on GUI
     */
    @FXML
    Text player1_kills, player2_kills, player3_kills, player4_kills;
    StringProperty player1KillProperty, player2KillProperty, player3KillProperty, player4KillProperty;
    /**
     * Players deaths shown on GUI
     */
    @FXML
    Text player1_deaths, player2_deaths, player3_deaths, player4_deaths;
    StringProperty player1DeathProperty, player2DeathProperty, player3DeathProperty, player4DeathProperty;
    /**
     * Players score shown on GUI
     */
    @FXML
    Text player1_score, player2_score, player3_score, player4_score;
    StringProperty player1ScoreProperty, player2ScoreProperty, player3ScoreProperty, player4ScoreProperty;
    /**
     * The section of rank 3th and 4 th pane
     */
    @FXML
    GridPane rank_3, rank_4;
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
    private ArrayList<Integer> leaderBoard;
    /**
     * Number of total players
     */
    int num_players;

    /**
     * Set the scoreboard of the game
     *
     * @param scoreBoard game's scoreboard
     */
    public void setScoreBoard(ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    /**
     * Set the number of total players of the game
     *
     * @param num_players total players of the game
     */
    public void setNum_players(int num_players) {
        this.num_players = num_players;
    }

    /**
     * Set the leaderboard of the game
     *
     * @param leaderBoard the leaderboard of the game
     */
    public void setLeaderBoard(ArrayList<Integer> leaderBoard) {
        this.leaderBoard = leaderBoard;
    }


    /**
     * Constructor for Initialising the text property
     */
    public LeaderboardController() {

        player1IdProperty = new SimpleStringProperty();
        player2IdProperty = new SimpleStringProperty();
        player3IdProperty = new SimpleStringProperty();
        player4IdProperty = new SimpleStringProperty();


        player1KillProperty = new SimpleStringProperty();
        player2KillProperty = new SimpleStringProperty();
        player3KillProperty = new SimpleStringProperty();
        player4KillProperty = new SimpleStringProperty();

        player1DeathProperty = new SimpleStringProperty();
        player2DeathProperty = new SimpleStringProperty();
        player3DeathProperty = new SimpleStringProperty();
        player4DeathProperty = new SimpleStringProperty();

        player1ScoreProperty = new SimpleStringProperty();
        player2ScoreProperty = new SimpleStringProperty();
        player3ScoreProperty = new SimpleStringProperty();
        player4ScoreProperty = new SimpleStringProperty();

    }

    /**
     * Update the leaderboard
     */
    public void update() {

        updateRank(0, player1IdProperty, player1DeathProperty, player1KillProperty, player1ScoreProperty);
        updateRank(1, player2IdProperty, player2DeathProperty, player2KillProperty, player2ScoreProperty);
        updateRank(2, player3IdProperty, player3DeathProperty, player3KillProperty, player3ScoreProperty);
        updateRank(2, player3IdProperty, player3DeathProperty, player3KillProperty, player3ScoreProperty);
        updateRank(3, player4IdProperty, player4DeathProperty, player4KillProperty, player4ScoreProperty);

    }

    /**
     * Update the information based on the rank
     * @param rank
     * @param idProperty
     * @param deathProperty
     * @param killProperty
     * @param scoreProperty
     */
    public void updateRank(int rank, StringProperty idProperty, StringProperty deathProperty, StringProperty killProperty, StringProperty scoreProperty) {
        int id = leaderBoard.get(rank);
        idProperty.set(String.valueOf(id));
        deathProperty.set(String.valueOf(scoreBoard.getPlayerDeaths(id)));
        killProperty.set(String.valueOf(scoreBoard.getPlayerKills(id)));
        scoreProperty.set(String.valueOf(scoreBoard.getPlayerScore(id)));
    }

    /**
     * Bind the text property to text
     * @param location  url location
     * @param resources resource bundled
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        player1_id.textProperty().bind(player1IdProperty);
        player2_id.textProperty().bind(player2IdProperty);
        player3_id.textProperty().bind(player3IdProperty);
        player4_id.textProperty().bind(player4IdProperty);


        player1_deaths.textProperty().bind(player1DeathProperty);
        player2_deaths.textProperty().bind(player2DeathProperty);
        player3_deaths.textProperty().bind(player3DeathProperty);
        player4_deaths.textProperty().bind(player4DeathProperty);

        player1_kills.textProperty().bind(player1KillProperty);
        player2_kills.textProperty().bind(player2KillProperty);
        player3_kills.textProperty().bind(player3KillProperty);
        player4_kills.textProperty().bind(player4KillProperty);


        player1_score.textProperty().bind(player1ScoreProperty);
        player2_score.textProperty().bind(player2ScoreProperty);
        player3_score.textProperty().bind(player3ScoreProperty);
        player4_score.textProperty().bind(player4ScoreProperty);


        if (num_players == 2) {
            rank_3.setVisible(false);
            rank_4.setVisible(false);
        } else if (num_players == 3) {
            rank_4.setVisible(false);
        }
    }

}
