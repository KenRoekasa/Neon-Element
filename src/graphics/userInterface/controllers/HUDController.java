package graphics.userInterface.controllers;


import client.ClientGameState;
import engine.GameState;
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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentLinkedDeque;

//To get players' health and speed in top-left hud
public class HUDController extends UIController implements Initializable{

    private ClientGameState gameState;

    public void setGameState(ClientGameState gameState) {
        this.gameState = gameState;
    }

    @FXML
    private Text health;
    private StringProperty healthValue;

    @FXML
    private Text kills;
    private StringProperty totalKills;

    public HUDController(){
        healthValue = new SimpleStringProperty();
        healthValue.set("100");
        totalKills  = new SimpleStringProperty();
        totalKills.set("0");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        health.textProperty().bind(healthValue);
        kills.textProperty().bind(totalKills);

    }

    public void update() {
        healthValue.set(String.valueOf((int)(gameState.getPlayer().getHealth())));
        totalKills.set(String.valueOf((int)(gameState.getScoreBoard().getPlayerKills(gameState.getPlayer().getId()))));
    }
}

