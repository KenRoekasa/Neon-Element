package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

//For mode board: online/local
public class ModeController implements Initializable{
    private Stage stage;
    private Rectangle2D stageSize;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setStageSize(Rectangle2D stageSize) {
        this.stageSize = stageSize;
    }

   //local -> local_setup
    @FXML
    public void handleLocalBtn(ActionEvent actionEvent){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/local_setup.fxml"));
        try {
            Pane root = loader.load();
            Scene scene =new Scene(root,stageSize.getWidth(),stageSize.getHeight());
            LocalController localController = loader.getController();
            localController.setStage(stage);
            localController.setStageSize(stageSize);
            stage.setTitle("Local Mode Configuration");
            System.out.println("haja local");
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException e) {
            System.out.println("crush in loading local setup board ");
            e.printStackTrace();
        }

    }

    //online->choose host/ join
    @FXML
    public void handleOnlineBtn(ActionEvent actionEvent){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/online_option.fxml"));
        try {
            Pane root = loader.load();
            Scene scene =new Scene(root,stageSize.getWidth(),stageSize.getHeight());
            OnlineModeController onlineModeController = loader.getController();

            onlineModeController.setStage(stage);
            onlineModeController.setStageSize(stageSize);
            stage.setTitle("Online Mode");
            System.out.println("hhhh online");
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
            System.out.println("@modecontroller onlinebtn "+actionEvent.toString());
            System.out.println(actionEvent.getTarget());

        } catch (IOException e) {
            System.out.println("crush in loading online option board ");
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
