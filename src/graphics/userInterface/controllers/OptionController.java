package graphics.userInterface.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

/**
 * Controller for option.fxml
 */
public class OptionController extends UIController{

    /**
     * Slider to adjust sound's volume
     */
    @FXML
    public Slider sound;

    /**
     * Text which shows the volume between 0-1.o
     */
    @FXML
    Text volume;

    /** Handle the action of pressing ok button which will set the volume.
     */
    @FXML
    public void handleOkBtn(){
        audioManager.setVolume(sound.getValue());
        System.out.println("sound value"+ sound.getValue());

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

        // Set slider properties
        // volume range:0 - 1.0
        // defalut value is 0.4
        sound.setMin(0);
        sound.setMax(1.0);
        sound.setValue(1);


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
