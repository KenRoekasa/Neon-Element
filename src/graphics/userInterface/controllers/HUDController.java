package graphics.userInterface.controllers;


import client.ClientGameState;
import engine.entities.Player;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

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

    public void initPlayer(Player player) {
       healthValue.set(String.valueOf(player.getHealth()+"/100.0 "));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Thread property = new Thread();

    }
}

