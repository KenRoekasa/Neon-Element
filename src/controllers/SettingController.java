package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import userInterface.Menu;

import java.io.IOException;

public class SettingController {
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private Stage stage;
    @FXML
    public void handleSoundVolume(){

    }

    @FXML
    public void handleOkBtn(){

    }
    @FXML
    public void handleBackBtn(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/menu.fxml"));

        try {
            Pane root = loader.load();
            stage.getScene().setRoot(root);
            MenuController controller = loader.getController();
            controller.setStage(stage);
            stage.setTitle("Menu");

        } catch (IOException e) {
            System.out.println("crush in loading menu board ");
            e.printStackTrace();
        }
    }


}
