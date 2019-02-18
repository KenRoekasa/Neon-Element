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
        //TODO: back to preserved-state game
    }

    //back to menu or game?
    @FXML
    public void handleSettingBtn(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/setting.fxml"));

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/menu.fxml"));
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