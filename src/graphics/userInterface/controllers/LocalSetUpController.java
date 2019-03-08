package graphics.userInterface.controllers;

import client.GameClient;
import client.ClientGameState;
import client.GameStateGenerator;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;


import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

//For local_setup scene

public class LocalSetUpController extends UIController implements Initializable {
    private ClientGameState gameState;
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

    private int enemy_num;
    private String enemy_1;
    private String enemy_2;
    private String enemy_3;
    private String selected_mode;
    ArrayList<String> enemyTypes = new ArrayList<>();

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

        selected_mode = String.valueOf(mode.getSelectedToggle().getUserData());

        switch (enemy_num) {
            case 1:
                enemy_1 = (String) diff_1.getSelectedToggle().getUserData();
                enemyTypes.add(enemy_1);
                break;
            case 2:
                enemy_1 = (String) diff_1.getSelectedToggle().getUserData();
                enemy_2 = (String) diff_2.getSelectedToggle().getUserData();
                enemyTypes.add(enemy_1);
                enemyTypes.add(enemy_2);
                break;
            case 3:
                enemy_1 = (String) diff_1.getSelectedToggle().getUserData();
                enemy_2 = (String) diff_2.getSelectedToggle().getUserData();
                enemy_3 = (String) diff_3.getSelectedToggle().getUserData();
                enemyTypes.add(enemy_1);
                enemyTypes.add(enemy_2);
                enemyTypes.add(enemy_3);

        }

        // create game rules
        // todo make this configurable
        gameState = GameStateGenerator.createDemoGamestateSample(enemy_num,enemyTypes);
        //g.getPlayer().getHealth();
        try {
            boolean networked = false;
            GameClient gameBoard = new GameClient(stage, gameState, networked, audioManager);
            Scene scene = gameBoard.getScene();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @FXML
    public void handleBackBtn(ActionEvent actionEvent) {
        String fxmlPath ="../fxmls/mode.fxml";
        String stageTitle ="Mode" ;
        String fileException ="Mode";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ToggleGroupSetUp.setToggleGroup(num_group,num_1,num_2,num_3);
        ToggleGroupSetUp.setToggleGroup(diff_1,easy_1,normal_1,hard_1);
        ToggleGroupSetUp.setToggleGroup(diff_2,easy_2,normal_2,hard_2);
        ToggleGroupSetUp.setToggleGroup(diff_3,easy_3,normal_3,hard_3);
        ToggleGroupSetUp.setToggleGroup(mode,life_mode,time_mode);


        ToggleGroupSetUp.setUserData("Easy",easy_1,easy_2,easy_3);
        ToggleGroupSetUp.setUserData("Normal",normal_1,normal_2,normal_3);
        ToggleGroupSetUp.setUserData("Hard",hard_1,hard_2,hard_3);

        num_1.setUserData(1);
        num_2.setUserData(2);
        num_3.setUserData(3);

        life_mode.setUserData("life_based");
        time_mode.setUserData("time_based");

    }
}

