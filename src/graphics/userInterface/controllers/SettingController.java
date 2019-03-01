package graphics.userInterface.controllers;

import client.audiomanager.AudioManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingController extends UIController{
    public Slider sound;
    @FXML
    Label ok,back;
    @FXML
    public void handleSoundVolume(){
        //todo this ?
        AudioManager.setVolume(sound.getValue());

    }

    @FXML
    public void handleOkBtn(){

    }
    @FXML
    public void handleBackBtn() {
        String fxmlPath = "../fxmls/menu_new.fxml";
        String stageTitle = "Menu";
        String fileException = "Menu";
        FxmlLoader loader = new FxmlLoader(fxmlPath, stage, stageTitle, fileException);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        back.setTextFill(outline);
        ok.setTextFill(outline);
        back.setEffect(blend);
        ok.setEffect(blend);
    }
}
