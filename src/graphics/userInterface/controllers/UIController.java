package graphics.userInterface.controllers;

import client.audiomanager.AudioManager;
import client.audiomanager.Sound;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Super class for all the user interface's controller
 */
public class UIController implements Initializable{
    /***
     * The stage of the whole game
     */
    protected Stage stage;
    /** The audio manager of the whole game
     * */
    protected AudioManager audioManager;

    /** Pass the stage of game to the next controller
     * @param stage the stage of the game
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    /** Get the stage of the game
     * @return the stage of the game
     */
    public Stage getStage() {
        return stage;
    }

    /** Pass the audio manager to the next controller
     * @param audioManager the audio manager of the game
     */
    public void setAudioManager(AudioManager audioManager) {
        this.audioManager = audioManager;
    }

    /** Get the audio manager of the game
     * @return the audio manager of the game
     */
    public AudioManager getAudioManager(){
        return this.audioManager;
    }


    /** For subclass to implement the initialize function of Initializable interface
     * @param location url location
     * @param resources resource bundled
     */


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void playHoverSound(){
        audioManager.playSound(Sound.LIGHT_HIT);
    }

}
