package controllers;

import entities.Player;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.awt.event.ActionEvent;

public class ValueController {
    private Player player;
    @FXML
    private Text health;
    @FXML
    private void changeHealthValue(ActionEvent actionEvent){
        health.setText(String.valueOf(player.getHealth()));
        System.out.println("Health value changed!");
    }
}
