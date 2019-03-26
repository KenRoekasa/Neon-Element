package graphics.userInterface.controllers;

import client.audiomanager.AudioManager;
import client.audiomanager.Sound;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

/**
 * A template for loading the fxml file and setting up corresponding controller
 * */
public class FxmlLoader {

    /**
     * The controller for the loaded fxml
     */
    private UIController controller;

    /**
     * Get the controller for the loaded fxml
     * @return controller the loaded fxml's controller
     */
    public UIController getController() {
        return controller;
    }

    /** Constructor for FxmlLoader to initialise and configure the scene
     * @param fxmlPath the relative path of fxml which is going to be loading
     * @param stage the stage of the application
     * @param stageTitle the stage title of current scene
     * @param fileException the custom string in exception to indicate where the exception is
     * @param audioManager the whole game's audio manger for controller the audio
     */
    protected FxmlLoader(String fxmlPath, Stage stage,String stageTitle,String fileException, AudioManager audioManager){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        try {
            Pane root = loader.load();
            stage.getScene().setRoot(root);
            controller = loader.getController();
            controller.setStage(stage);
            controller.setAudioManager(audioManager);
            stage.setTitle(stageTitle);
            playButtonSound(audioManager);

        } catch (IOException e) {
            System.out.println("Crush in loading" +fileException+".fxml file.");
            e.printStackTrace();
        }
    }



    public void playButtonSound(AudioManager audioManager) {
        // pick a random button pitch
        int sound = new Random().nextInt(4);

        switch (sound) {
            case 0:
                audioManager.playSound(Sound.BUTTON1);
                break;
            case 1:
                audioManager.playSound(Sound.BUTTON2);
                break;
            case 2:
                audioManager.playSound(Sound.BUTTON3);
                break;
            case 3:
                audioManager.playSound(Sound.BUTTON4);
                break;
        }
    }
}
