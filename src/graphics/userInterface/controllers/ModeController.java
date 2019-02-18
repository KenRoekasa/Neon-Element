package graphics.userInterface.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

//For mode board: online/local
public class ModeController implements Initializable{
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

   //local -> local_setup
    @FXML
    public void handleLocalBtn(ActionEvent actionEvent){
<<<<<<< HEAD:src/controllers/ModeController.java
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/fxml/local_setup.fxml"));
=======
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/local_setup.fxml"));
>>>>>>> 57ad7f93eb51328661503183acca5c860c7e0fd2:src/graphics/userInterface/controllers/ModeController.java
        try {
            Pane root = loader.load();
            stage.getScene().setRoot(root);
            LocalSetUpController controller = loader.getController();
            controller.setStage(stage);
            stage.setTitle("Local Mode Configuration");
        } catch (IOException e) {
            System.out.println("crush in loading local setup board ");
            e.printStackTrace();
        }

    }

    //online->choose host/ join
    @FXML
    public void handleOnlineBtn(ActionEvent actionEvent){
<<<<<<< HEAD:src/controllers/ModeController.java
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/fxml/online_mode.fxml"));
=======
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/online_mode.fxml"));
>>>>>>> 57ad7f93eb51328661503183acca5c860c7e0fd2:src/graphics/userInterface/controllers/ModeController.java
        try {
            Pane root = loader.load();
            stage.getScene().setRoot(root);
            OnlineModeController controller = loader.getController();

            controller.setStage(stage);
            stage.setTitle("Online Mode");


        } catch (IOException e) {
            System.out.println("crush in loading online option board ");
            e.printStackTrace();
        }
    }
    //back->mode board
    @FXML
    public void handleBackBtn(ActionEvent actionEvent){
<<<<<<< HEAD:src/controllers/ModeController.java
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/fxml/menu.fxml"));
        try {
=======
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/menu.fxml"));
            try {
>>>>>>> 57ad7f93eb51328661503183acca5c860c7e0fd2:src/graphics/userInterface/controllers/ModeController.java
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
