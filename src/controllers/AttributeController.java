package controllers;

import entities.Player;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

//To get players' health and speed in top-left hud
public class AttributeController implements Initializable {
    @FXML
    private Text health;
    private StringProperty healthValue;
    @FXML
    private Text speed;
    private StringProperty speedValue;

    public AttributeController(){
        healthValue = new SimpleStringProperty();
        speedValue = new SimpleStringProperty();
        healthValue.set("100");
        speedValue.set("5");
    }

    public void initPlayer(Player player) {
       healthValue.set(String.valueOf(player.getHealth()+"/100.0 "));
       speedValue.set(String.valueOf(player.getMovementSpeed()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        health.textProperty().bind(healthValue);
        speed.textProperty().bind(speedValue);
    }
}

