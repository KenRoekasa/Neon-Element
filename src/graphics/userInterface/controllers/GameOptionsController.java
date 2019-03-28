package graphics.userInterface.controllers;

import client.ClientGameState;
import client.audiomanager.AudioManager;
import client.audiomanager.Sound;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

/**
 * Controller for options_game.fxml which in the options of pause
 */
public class GameOptionsController extends UIController{

    /**
     * Slider to adjust sound's volume
     */
    @FXML
    public Slider sound, music;
    /**
     * Text which shows the volume between 0..0-100.0
     */
    @FXML
    Text volume, musicVolume;
    /**
     * The hud pane
     */
    private Pane hudPane;
    /**
     * The node pane
     */
    private Pane node;
    /**
     * The current client game state
     */
    private ClientGameState gamestate;

    /** Set the hud pane
     * @param hudPane
     */
    public void setHudPane(Pane hudPane) {
        this.hudPane = hudPane;
    }

    /** Set the subnode pane
     * @param node
     */
    public void setNode(Pane node) {
        this.node = node;
    }

    /** Set the game state
     * @param gameState the game state of the game
     */
    public void setGamestate(ClientGameState gameState) {
        this.gamestate = gameState;
    }

    /**
    /**
     * Handle the action of pressing back button which will go to game board
     */
    @FXML
    public void handleBackBtn(){
        hudPane.getChildren().remove(node);
        gamestate.resume();

    }

    /** Handle the action of pressing ok button which will set the volume.
     */
    @FXML
    public void handleOkBtn(){
        audioManager.setEffectVolume(sound.getValue()/ 100);
        audioManager.setMusicVolume(music.getValue()/ 100);
        audioManager.playSound(Sound.BUTTON4);

        hudPane.getChildren().remove(node);
        gamestate.resume();

    }
    /** Initialise the set up of slider and add listener to slider
     * @param location  url location
     * @param resources resource bundled
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        sound.setMin(0);
        sound.setMax(100);
        sound.setValue(50);

        DecimalFormat df = new DecimalFormat("0.0");
        sound.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> volume.textProperty().setValue(
                String.valueOf(df.format(sound.getValue()))));

        music.setMin(0);
        music.setMax(100);
        music.setValue(50);

        DecimalFormat df2 = new DecimalFormat("0.0");
        music.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> musicVolume.textProperty().setValue(
                String.valueOf(df2.format(music.getValue()))));

    }

    /**
     * Update the volume to the slider and label everytime enter options setting page
     */

    void updateVolume(){
        if(audioManager.getEffectVolume()==0){
            volume.setText("0.0");
        }

        if(audioManager.getMusicVolume()==0){
            musicVolume.setText("0.0");
        }
        sound.setValue(audioManager.getEffectVolume() * 100);
        music.setValue(audioManager.getMusicVolume() * 100);
    }
}

