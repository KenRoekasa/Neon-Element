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

public class SettingController extends UIController{
    public Slider sound;
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

}
