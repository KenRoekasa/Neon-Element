package graphics.userInterface.controllers;

import client.audiomanager.AudioManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
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
