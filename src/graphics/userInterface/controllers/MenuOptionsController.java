package graphics.userInterface.controllers;

import client.audiomanager.AudioManager;
import client.audiomanager.Sound;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

/**
 * Controller for options_menu.fxml
 */
public class MenuOptionsController extends UIController{

    /**
     * Slider to adjust sound's volume
     */
    @FXML
    public Slider sound, music;



    /**
     * Text which shows the volume between 0-1.o
     */
    @FXML
    Text volume, musicVolume;

    /** Handle the action of pressing ok button which will set the volume.
     */
    @FXML
    public void handleOkBtn(){
        audioManager.setEffectVolume(sound.getValue());
        audioManager.setMusicVolume(music.getValue());
        audioManager.playSound(Sound.BUTTON4);


    }

    /**Handle the action of pressing back button which will go back to menu.fxml
     */
    @FXML
    public void handleBackBtn() {
        String fxmlPath = "../fxmls/menu.fxml";
        String stageTitle = "Menu";
        String fileException = "Menu";
        FxmlLoader loader = new FxmlLoader(fxmlPath, stage, stageTitle, fileException, audioManager);
    }

    /** Initialise the set up of slider and add listener to slider
     * @param location  url location
     * @param resources resource bundled
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        sound.setMin(0);
        sound.setMax(100);



        DecimalFormat df = new DecimalFormat("0.0");
        sound.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> volume.textProperty().setValue(
                String.valueOf(df.format(sound.getValue()))));


        music.setMin(0);
        music.setMax(100);

        DecimalFormat df2 = new DecimalFormat("0.0");
        music.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> musicVolume.textProperty().setValue(
                String.valueOf(df2.format(music.getValue()))));

    }

    /**
     * Update the changed volume everytime click the options button on menu page
     */

    public void updateSlider(){
        sound.setValue(audioManager.getEffectVolume());
        music.setValue(audioManager.getMusicVolume());
    }
}
