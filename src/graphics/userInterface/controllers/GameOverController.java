package graphics.userInterface.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class GameOverController {

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private Stage stage;

    public void handleQuitBtn(ActionEvent actionEvent) {
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
