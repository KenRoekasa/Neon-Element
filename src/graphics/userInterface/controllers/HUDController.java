package graphics.userInterface.controllers;

<<<<<<< HEAD:src/controllers/HUDController.java
import entities.Player;
import enumSwitches.colourSwitch;
import enums.Elements;
=======
import engine.entities.Player;
>>>>>>> 57ad7f93eb51328661503183acca5c860c7e0fd2:src/graphics/userInterface/controllers/HUDController.java
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
    private Text kills;
    private StringProperty totalKills;


    @FXML
    private Circle fire,water,earth,air;


    public HUDController(){
        healthValue = new SimpleStringProperty();
        healthValue.set("100");
        totalKills  = new SimpleStringProperty();
        totalKills.set("0");
    }

    @FXML
    public void changeElement(ActionEvent event,Player player) {
        Elements e = player.getCurrentElement();
        Color colour = colourSwitch.getElementColour(e);
    }

    @FXML
    public void handlePauseBtn(){
<<<<<<< HEAD:src/controllers/HUDController.java
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/fxml/pause.fxml"));
=======
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/pause.fxml"));
>>>>>>> 57ad7f93eb51328661503183acca5c860c7e0fd2:src/graphics/userInterface/controllers/HUDController.java
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
       //totalKills.set(String.valueof()); //get kills value here
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        health.textProperty().bind(healthValue);
        //kills.textProperty().bind(totalKills);
    }
}

