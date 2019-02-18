package graphics.userInterface.controllers;

import client.ClientGameState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/* Menu buttons:
1. Play
2. Options
3. Help
4. Exit
*/

public class MenuController implements Initializable{
    private Stage stage;
    private ClientGameState gameState;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // play -> mode selection
    @FXML
    public void handlePlayBtn(ActionEvent actionEvent) {

        // create game rules
        // todo make this configurable
        
      //select mode
<<<<<<< HEAD:src/controllers/MenuController.java
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/fxml/mode_board.fxml"));
=======
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/mode_board.fxml"));
>>>>>>> 57ad7f93eb51328661503183acca5c860c7e0fd2:src/graphics/userInterface/controllers/MenuController.java

        try {
            Pane root = loader.load();
            stage.getScene().setRoot(root);
            ModeController controller = loader.getController();
            controller.setStage(stage);
            stage.setTitle("Mode");

        } catch (IOException e) {
            System.out.println("crush in loading mode board ");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSettingBtn(ActionEvent actionEvent){
        //select mode
<<<<<<< HEAD:src/controllers/MenuController.java
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/fxml/setting.fxml"));
=======
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/setting.fxml"));
>>>>>>> 57ad7f93eb51328661503183acca5c860c7e0fd2:src/graphics/userInterface/controllers/MenuController.java

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
    public void handleHelpBtn(ActionEvent actionEvent){
        //select mode
<<<<<<< HEAD:src/controllers/MenuController.java
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/fxml/help.fxml"));
=======
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/help.fxml"));
>>>>>>> 57ad7f93eb51328661503183acca5c860c7e0fd2:src/graphics/userInterface/controllers/MenuController.java
        try {
            Pane root = loader.load();
            stage.getScene().setRoot(root);
            SettingController controller = loader.getController();
            controller.setStage(stage);
            stage.setTitle("GUIDE");

        } catch (IOException e) {
            System.out.println("crush in loading setting board ");
            e.printStackTrace();
        }

    }

    @FXML
    public void handleExitBtn(ActionEvent actionEvent){
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
