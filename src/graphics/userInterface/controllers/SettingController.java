package graphics.userInterface.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
<<<<<<< HEAD:src/controllers/SettingController.java
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/fxml/menu.fxml"));
=======
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/menu.fxml"));
>>>>>>> 57ad7f93eb51328661503183acca5c860c7e0fd2:src/graphics/userInterface/controllers/SettingController.java

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
