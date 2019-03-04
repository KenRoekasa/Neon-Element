package graphics.userInterface.controllers;

import client.audiomanager.AudioManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class OptionController extends UIController{

    @FXML
    public Slider sound;

    @FXML
    Label ok,back;

    @FXML
    Text volume;


    @FXML
    public void handleOkBtn(){
        AudioManager.setVolume(sound.getValue());
        System.out.println("sound value"+ sound.getValue());

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


        // Set slider properties
        // volume range:0 - 1.0
        // defalut value is 0.4
        sound.setMin(0);
        sound.setMax(1.0);
        sound.setValue(0.4);


        DecimalFormat df = new DecimalFormat("0.0");
        sound.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                volume.textProperty().setValue(
                        String.valueOf(df.format(sound.getValue())));

            }
        });

    }
}
