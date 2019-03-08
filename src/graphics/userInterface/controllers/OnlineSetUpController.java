package graphics.userInterface.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class OnlineSetUpController extends UIController implements Initializable{


    public RadioButton player_1,player_2,player_3,num_1, num_2, num_3;
    public RadioButton easy_1, easy_2, easy_3, normal_1, normal_2, normal_3, hard_1, hard_2, hard_3;
    public RadioButton time_mode, life_mode;
    public GridPane enemy2, enemy3;

    public Button back_btn;

    public Button create_btn;

    final ToggleGroup num_group = new ToggleGroup();
    final ToggleGroup num_player = new ToggleGroup();
    final ToggleGroup diff_1 = new ToggleGroup();
    final ToggleGroup diff_2 = new ToggleGroup();
    final ToggleGroup diff_3 = new ToggleGroup();
    final ToggleGroup mode = new ToggleGroup();


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
    public void handleCreateBtn(){
        String fxmlPath = "../fxmls/ip_host.fxml";
        String stageTitle ="Host a Game" ;
        String fileException ="IP Host";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }

    @FXML
    public void handleBackBtn(){
        //select mode
        String fxmlPath = "../fxmls/online_mode.fxml";
        String stageTitle = "Online Configuration";
        String fileException ="Online Setup";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ToggleGroupSetUp.setToggleGroup(num_player,player_1,player_2,player_3);
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
