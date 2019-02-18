package graphics.userInterface.controllers;

import engine.entities.Player;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

//To get players' health and speed in top-left hud
public class HUDController implements Initializable{
    private Stage stage;
    @FXML
    public Button pause;
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    private Text health;
    private StringProperty healthValue;
    @FXML
    private Text speed;
    private StringProperty speedValue;

    public HUDController(){
        healthValue = new SimpleStringProperty();
        speedValue = new SimpleStringProperty();
        healthValue.set("100");
        speedValue.set("5");
    }

    @FXML
    public void handlePauseBtn(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/pause.fxml"));
        try {
            Pane root = loader.load();
            stage.getScene().setRoot(root);
            PauseController controller = loader.getController();
            controller.setStage(stage);
            stage.setTitle("Pause");

        } catch (IOException e) {
            System.out.println("crush in loading pause board ");
            e.printStackTrace();
        }
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

