package graphics.userInterface.controllers;

import client.audiomanager.AudioManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
/*Extract duplicate process on loading fxml file and its configuration*/
public class FxmlLoader {
	private UIController controller;
	
	public UIController getController() {
			return controller;
	}
    protected FxmlLoader(String fxmlPath, Stage stage,String stageTitle,String fileException, AudioManager audioManager){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        try {
            Pane root = loader.load();
            stage.getScene().setRoot(root);
            controller = loader.getController();
            controller.setStage(stage);
            controller.setAudioManager(audioManager);
            stage.setTitle(stageTitle);


        } catch (IOException e) {
            System.out.println("Crush in loading" +fileException+".fxml file.");
            e.printStackTrace();
        }
    }


}
