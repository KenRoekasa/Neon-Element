package graphics.userInterface.controllers;

import client.audiomanager.AudioManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * A template for loading the fxml file and setting up corresponding controller
 * */
public class FxmlLoader {

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
            UIController controller = loader.getController();
            controller.setStage(stage);
            controller.setAudioManager(audioManager);
            stage.setTitle(stageTitle);


        } catch (IOException e) {
            System.out.println("Crush in loading" +fileException+".fxml file.");
            e.printStackTrace();
        }
    }
}
