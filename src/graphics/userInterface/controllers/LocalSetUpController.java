package graphics.userInterface.controllers;

import client.ClientBoard;
import client.ClientGameState;
import client.GameStateGenerator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

public class LocalSetUpController implements Initializable {
    private ClientGameState gameState;
    private Stage stage;
    @FXML
    public RadioButton num_1, num_2, num_3;
    @FXML
    public RadioButton easy_1, easy_2, easy_3, normal_1, normal_2, normal_3, hard_1, hard_2, hard_3;
    @FXML
    public RadioButton time_mode, life_mode;

    @FXML
    GridPane enemy2, enemy3;
    final ToggleGroup num_group = new ToggleGroup();
    final ToggleGroup diff_1 = new ToggleGroup();
    final ToggleGroup diff_2 = new ToggleGroup();
    final ToggleGroup diff_3 = new ToggleGroup();
    final ToggleGroup mode = new ToggleGroup();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private int enemy_num;
    private String enemy_1;
    private String enemy_2;
    private String enemy_3;
    private String selected_mode;

    public int getEnemy_num() {
        return enemy_num;
    }

    public String getEnemy_1() {
        return enemy_1;
    }

    public String getEnemy_2() {
        return enemy_2;
    }

    public String getEnemy_3() {
        return enemy_3;
    }

    public String getSelected_mode() {
        return selected_mode;
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
    public void handleStartBtn(ActionEvent actionEvent) {
        //get selected properties
        enemy_num = (int) num_group.getSelectedToggle().getUserData();
        System.out.println("@localController Number of enemies: " + enemy_num);

        selected_mode = String.valueOf(mode.getSelectedToggle().getUserData());
        System.out.println("@localController Selected mode: " + selected_mode);

        switch (enemy_num) {
            case 1:
                enemy_1 = (String) diff_1.getSelectedToggle().getUserData();
                System.out.println("@localController Difficulty of Enemy 1: " + enemy_1);
                break;
            case 2:
                enemy_1 = (String) diff_1.getSelectedToggle().getUserData();
                enemy_2 = (String) diff_2.getSelectedToggle().getUserData();
                System.out.println("@localController Difficulty of Enemy 1: " + enemy_1);
                System.out.println("@localController Difficulty of Enemy 2: " + enemy_2);
                break;
            case 3:
                enemy_1 = (String) diff_1.getSelectedToggle().getUserData();
                enemy_2 = (String) diff_2.getSelectedToggle().getUserData();
                enemy_3 = (String) diff_2.getSelectedToggle().getUserData();
                System.out.println("@localController Difficulty of Enemy 1: " + enemy_1);
                System.out.println("@localController Difficulty of Enemy 2: " + enemy_2);
                System.out.println("@localController Difficulty of Enemy 3: " + enemy_3);
        }

        // create game rules
        // todo make this configurable
        gameState = GameStateGenerator.createDemoGamestateSample(enemy_num);
        //g.getPlayer().getHealth();
        try {
            ClientBoard gameBoard = new ClientBoard(stage, gameState);
            Scene scene = gameBoard.getScene();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @FXML
    public void handleBackBtn(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmls/mode_board.fxml"));

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
        num_3.setSelected(true);

        easy_1.setToggleGroup(diff_1);
        normal_1.setToggleGroup(diff_1);
        hard_1.setToggleGroup(diff_1);
        easy_1.setSelected(true);

        easy_2.setToggleGroup(diff_2);
        normal_2.setToggleGroup(diff_2);
        hard_2.setToggleGroup(diff_2);
        easy_2.setSelected(true);

        easy_3.setToggleGroup(diff_3);
        normal_3.setToggleGroup(diff_3);
        hard_3.setToggleGroup(diff_3);
        easy_3.setSelected(true);

        time_mode.setToggleGroup(mode);
        life_mode.setToggleGroup(mode);
        time_mode.setSelected(true);

        num_1.setUserData(1);
        num_2.setUserData(2);
        num_3.setUserData(3);

        life_mode.setUserData("life_based");
        time_mode.setUserData("time_based");

        easy_1.setUserData("Easy");
        easy_2.setUserData("Easy");
        easy_3.setUserData("Easy");
        normal_1.setUserData("Normal");
        normal_2.setUserData("Normal");
        normal_3.setUserData("Normal");
        hard_1.setUserData("Hard");
        hard_2.setUserData("Hard");
        hard_3.setUserData("Hard");
    }

}
