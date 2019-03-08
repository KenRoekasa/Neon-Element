package graphics.userInterface.controllers;

import client.audiomanager.AudioManager;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UIController implements Initializable{
    protected Stage stage;
    protected AudioManager audioManager;

    UIController(){
        //javafx8 doesn't support to define shadow in css while javafx11 supports

    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setAudioManager(AudioManager audioManager) {
        this.audioManager = audioManager;
    }

    public AudioManager getAudioManager(){
        return this.audioManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
