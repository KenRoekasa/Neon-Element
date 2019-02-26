package graphics.userInterface.controllers;

import client.ClientGameState;
import javafx.application.Platform;
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

        String fxmlPath;
        String stageTitle;
        String fileException;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/mode.fxml"));

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/option.fxml"));

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/help.fxml"));
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

       // todo make this graceful

        Platform.exit();
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
