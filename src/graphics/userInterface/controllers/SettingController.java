package graphics.userInterface.controllers;

import client.audiomanager.AudioManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingController {
    public Slider sound;

    public void setStage(Stage stage) {
        this.stage = stage;


    }

    private Stage stage;
    @FXML
    public void handleSoundVolume(){
        //todo this ?
        AudioManager.setVolume(sound.getValue());

    }

    @FXML
    public void handleOkBtn(){

    }
    @FXML
    public void handleBackBtn(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/menu.fxml"));

        try {
            Pane root = loader.load();
            stage.getScene().setRoot(root);
            MenuController controller = loader.getController();
            controller.setStage(stage);
            stage.setTitle("Menu");

        } catch (IOException e) {
            System.out.println("crush in loading menu board ");
            e.printStackTrace();
        }
    }


}
