package graphics.userInterface.controllers;


import client.ClientGameState;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

//To get players' health and speed in top-left hud
public class HUDController extends UIController implements Initializable {

    private ClientGameState gameState;

    private final static String TEMP = "Player %s with %s ";

    @FXML
    private Text health;
    private StringProperty healthValue;

    @FXML
    private Text kills;
    private StringProperty totalKills;

    @FXML Text death;
    private StringProperty deathTimes;

    @FXML
    private Label player1, player2, player3, player4;
    private StringProperty player1Property, player2Property, player3Property, player4Property;

    private int num_player;

    public HUDController() {
        healthValue = new SimpleStringProperty();
        totalKills = new SimpleStringProperty();
        deathTimes = new SimpleStringProperty();
        player1Property = new SimpleStringProperty();
        player2Property = new SimpleStringProperty();
        player3Property = new SimpleStringProperty();
        player4Property = new SimpleStringProperty();
    }

    public void setGameState(ClientGameState gameState) {
        this.gameState = gameState;
        this.num_player = gameState.getScoreBoard().getLeaderBoard().size();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        health.textProperty().bind(healthValue);
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

    private Integer getPlayerIdAtPosition(int index) {
        return this.gameState.getScoreBoard().getLeaderBoard().get(index);
    }

    public void update() {

        if (gameState.getPlayer().getHealth() < 0) {
            healthValue.set("0");
        } else {
            healthValue.set(String.valueOf((int) (gameState.getPlayer().getHealth())));
        }

        totalKills.set(String.valueOf((int) (gameState.getScoreBoard().getPlayerKills(this.gameState.getClientId()))));

        deathTimes.set(String.valueOf((int)(gameState.getScoreBoard().getPlayerDeaths(this.gameState.getClientId()))));

        String s1 = String.format(TEMP, getPlayerIdAtPosition(0).toString(), this.gameState.getScoreBoard().getPlayerScore(getPlayerIdAtPosition(0)));
        String s2 = String.format(TEMP, getPlayerIdAtPosition(1).toString(), this.gameState.getScoreBoard().getPlayerScore(getPlayerIdAtPosition(1)));
        player1Property.set(s1);
        player2Property.set(s2);

        if (num_player == 3) {
            String s3 = String.format(TEMP, getPlayerIdAtPosition(2).toString(), this.gameState.getScoreBoard().getPlayerScore(getPlayerIdAtPosition(2)));
            player3Property.set(s3);
        } else if (num_player == 4) {
            String s3 = String.format(TEMP, getPlayerIdAtPosition(2).toString(), this.gameState.getScoreBoard().getPlayerScore(getPlayerIdAtPosition(2)));
            String s4 = String.format(TEMP, getPlayerIdAtPosition(3).toString(), this.gameState.getScoreBoard().getPlayerScore(getPlayerIdAtPosition(3)));
            player3Property.set(s3);
            player4Property.set(s4);
        }
    }
}

