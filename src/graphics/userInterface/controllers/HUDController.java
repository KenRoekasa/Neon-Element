package graphics.userInterface.controllers;


import client.ClientGameState;
import engine.GameState;
import engine.ScoreBoard;
import engine.entities.Player;
import engine.enums.Elements;
import graphics.enumSwitches.colourSwitch;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentLinkedDeque;

//To get players' health and speed in top-left hud
public class HUDController extends UIController implements Initializable{

    private ClientGameState gameState;
    private ScoreBoard scoreBoard;

    public void setScoreBoard(ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    public void setLeaderBoard(ArrayList<Integer> leaderBoard) {
        this.leaderBoard = leaderBoard;
    }

    private ArrayList<Integer> leaderBoard;
    private final static String TEMP = "Player %s with %s ";

    public void setGameState(ClientGameState gameState) {
        this.gameState = gameState;
    }

    @FXML
    private Text health;
    private StringProperty healthValue;


    @FXML
    private Text kills;
    private StringProperty totalKills;

    @FXML
    private Label player1,player2,player3,player4;
    private StringProperty player1Property,player2Property,player3Property,player4Property;

    private int num_player;

    public void setNum_player(int num_player) {
        this.num_player = num_player;
    }
    public HUDController(){
        healthValue = new SimpleStringProperty();
        healthValue.set("100");
        totalKills  = new SimpleStringProperty();
        totalKills.set("0");
        player1Property = new SimpleStringProperty();
        player1Property.set("0");
        player2Property = new SimpleStringProperty();
        player2Property.set("0");
        player3Property = new SimpleStringProperty();
        player3Property.set("0");
        player4Property = new SimpleStringProperty();
        player4Property.set("0");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        health.textProperty().bind(healthValue);
        kills.textProperty().bind(totalKills);

        player1.textProperty().bind(player1Property);
        player2.textProperty().bind(player2Property);
        player3.textProperty().bind(player3Property);
        player4.textProperty().bind(player4Property);

        switch (num_player){
            case 2: {
                player3.setVisible(false);
                player4.setVisible(false);
                break;
            }

            case 3:{
                player3.setVisible(true);
                player4.setVisible(false);
                break;
            }
            case 4:{
                player3.setVisible(true);
                player4.setVisible(true);
            }
        }
    }

    public void update() {
        healthValue.set(String.valueOf((int)(gameState.getPlayer().getHealth())));
        totalKills.set(String.valueOf((int)(gameState.getScoreBoard().getPlayerKills(gameState.getPlayer().getId()))));

        String s1 = String.format(TEMP,leaderBoard.get(0).toString(),scoreBoard.getPlayerKills(leaderBoard.get(0)));
        String s2 = String.format(TEMP,leaderBoard.get(1).toString(),scoreBoard.getPlayerKills(leaderBoard.get(1)));
        player1Property.set(s1);
        player2Property.set(s2);

        if(num_player==3){
            String s3 = String.format(TEMP,leaderBoard.get(2).toString(),scoreBoard.getPlayerKills(leaderBoard.get(2)));
            player3Property.set(s3);
        }else {
            String s3 = String.format(TEMP,leaderBoard.get(2).toString(),scoreBoard.getPlayerKills(leaderBoard.get(2)));
            String s4 = String.format(TEMP,leaderBoard.get(3).toString(),scoreBoard.getPlayerKills(leaderBoard.get(3)));
            player3Property.set(s3);
            player4Property.set(s4);
        }
    }
}

