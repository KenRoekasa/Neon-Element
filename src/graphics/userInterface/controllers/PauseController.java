package graphics.userInterface.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

//Press space to pause the game
public class PauseController {
    private Stage stage;
    private Pane hudPane;

    public void setNode(Pane node) {
        this.node = node;
    }

    private  Pane node;

    public void setHudPane(Pane hudPane) {
        this.hudPane = hudPane;
    }

    @FXML

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void handleResumeBtn(){
        hudPane.getChildren().remove(node);
    }

    //back to menu or game?
    // todo this needs to return to the game, probably want an options menu thats transparent like the pause menu
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
    // todo this needs to end the game thread somehow
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
