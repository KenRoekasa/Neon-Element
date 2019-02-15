package controllers;

import client.ClientBoard;
import client.GameState;
import client.GameStateGenerator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

//For local_setup scene
public class LocalController  implements Initializable {
    private GameState gameState;
    private Stage stage;
    @FXML
    public RadioButton num_1, num_2, num_3;
    @FXML
    public RadioButton easy_1,easy_2,easy_3,normal_1,normal_2,normal_3,hard_1,hard_2,hard_3;
    @FXML
    public RadioButton time_mode,life_mode;

    @FXML
    GridPane enemy2, enemy3;
    final ToggleGroup num_group =new ToggleGroup();
    final ToggleGroup diff_1 = new ToggleGroup();
    final ToggleGroup diff_2 =new ToggleGroup();
    final ToggleGroup diff_3 = new ToggleGroup();
    final ToggleGroup mode = new ToggleGroup();
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // directly go to local mode map
    @FXML
    public void handleOneEnemy(ActionEvent event) {
        enemy2.setVisible(false);
        enemy3.setVisible(false);
    }

    @FXML
    public void handleTwoEnemies(ActionEvent event) {
        enemy2.setVisible(true);
        enemy3.setVisible(false);
    }

    @FXML
    public void handleThreeEnemies(ActionEvent event) {
        enemy2.setVisible(true);
        enemy3.setVisible(true);
    }


    @FXML
    public void handleStartBtn(javafx.event.ActionEvent actionEvent) {
        // create game rules
        // todo make this configurable
        gameState = GameStateGenerator.createDemoGamestate();
        //g.getPlayer().getHealth();
        try {
            ClientBoard gameBoard = new ClientBoard(stage, gameState);
            Scene scene = gameBoard.getScene();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @FXML
    public void handleBackBtn(javafx.event.ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../userInterface/mode_board.fxml"));

        try {
            Pane root = loader.load();
            stage.getScene().setRoot(root);
            ModeController modeController = loader.getController();
            modeController.setStage(stage);
            stage.setTitle("Local Mode");

        } catch (IOException e) {
            System.out.println("crush in loading mode board ");
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        num_1.setToggleGroup(num_group);
        num_2.setToggleGroup(num_group);
        num_3.setToggleGroup(num_group);

        easy_1.setToggleGroup(diff_1);
        normal_1.setToggleGroup(diff_1);
        hard_1.setToggleGroup(diff_1);

        easy_2.setToggleGroup(diff_2);
        normal_2.setToggleGroup(diff_2);
        hard_2.setToggleGroup(diff_2);

        easy_3.setToggleGroup(diff_3);
        normal_3.setToggleGroup(diff_3);
        hard_3.setToggleGroup(diff_3);

        time_mode.setToggleGroup(mode);
        life_mode.setToggleGroup(mode);
    }

}

