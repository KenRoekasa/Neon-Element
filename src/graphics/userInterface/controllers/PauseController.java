package graphics.userInterface.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

//Press space to pause the game
public class PauseController {
    private Stage stage;
    @FXML

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void handleResumeBtn(){
        //preserve game state

    }
    @FXML
    public void handleSettingBtn(){
        //select mode
<<<<<<< HEAD:src/controllers/PauseController.java
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/fxml/setting.fxml"));
=======
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/setting.fxml"));
>>>>>>> 57ad7f93eb51328661503183acca5c860c7e0fd2:src/graphics/userInterface/controllers/PauseController.java

        try {
            Pane root = loader.load();
            stage.getScene().setRoot(root);
            SettingController controller = loader.getController();
            controller.setStage(stage);
            stage.setTitle("Mode");

        } catch (IOException e) {
            System.out.println("crush in loading setting board ");
            e.printStackTrace();
        }
    }
    @FXML
    public void handleQuitBtn(){
<<<<<<< HEAD:src/controllers/PauseController.java
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/fxml/menu.fxml"));
=======
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/menu.fxml"));
>>>>>>> 57ad7f93eb51328661503183acca5c860c7e0fd2:src/graphics/userInterface/controllers/PauseController.java
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
