package graphics.userInterface.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for online_setup.fxml
 * */
public class OnlineSetUpController extends UIController {
    /**
     * Radio buttons of different number of player and different number of ai bots
     */
    @FXML
    public RadioButton player_1, player_2, player_3, player_4, num_1, num_2, num_3;

    /**
     * Radio buttons of three difficulties of three different number of ai bots
     */
    @FXML
    public RadioButton easy_1, easy_2, easy_3, normal_1, normal_2, normal_3, hard_1, hard_2, hard_3;
    /**
     * Radio buttons of modes
     */
    @FXML
    public RadioButton time_mode, life_mode;
    /**
     * Grid pane for ai bot2 and ai bot3 difficulty selection line
     */
    @FXML
    public GridPane enemy2, enemy3;
    /**
     * Text for alert the user once the selection is invalid
     */
    @FXML
    public Text alert;
    /**
     * Back button
     */
    @FXML
    public Button back_btn;
    /**
     * Create game button
     */
    @FXML
    public Button create_btn;

    /**
     * The number of ai bots
     */
    private int enemy_num;

    /**
     * The number of players
     */
    private int player_num;
    /**
     * The selected difficulties for ai bots
     */
    private String enemy_1, enemy_2, enemy_3;
    /**
     * The selectd mode for the game
     */
    private String selected_mode;

    /**
     * The list which contains the selected difficulties of ai bots
     */
    private ArrayList<String> enemyTypes = new ArrayList<>();

    /**
     * The toggle group of number of ai bots
     */
    final ToggleGroup num_group = new ToggleGroup();
    /**
     * The toggle group of number of players
     */
    final ToggleGroup num_player = new ToggleGroup();
    /**
     * The toggle group of first ai bot's difficulties optiona
     */
    final ToggleGroup diff_1 = new ToggleGroup();
    /**
     * The toggle group of second ai bot's difficulties optiona
     */
    final ToggleGroup diff_2 = new ToggleGroup();
    /**
     * The toggle group of third ai bot's difficulties optiona
     */
    final ToggleGroup diff_3 = new ToggleGroup();
    /**
     * The toggle group of game mode
     */
    final ToggleGroup mode = new ToggleGroup();

    /** Get the number of players
     * @return
     */
    public int getPlayer_num() {
        return player_num;
    }

    /**Get the list of ai bots' type
     * @return enemyTypes the list contains the type of ai bots
     */
    // todo when initialise the game, get the ai list type
    public ArrayList<String> getEnemyTypes() {
        return enemyTypes;
    }

    /** Handle the action when select the number of one ai bot
     */
    @FXML
    public void handleOneEnemy(ActionEvent event) {
        enemy2.setVisible(false);
        enemy3.setVisible(false);
    }

    /** Handle the action when select the number of two ai bots
     */
    @FXML
    public void handleTwoEnemies(ActionEvent event) {
        enemy2.setVisible(true);
        enemy3.setVisible(false);
    }

    /** Handle the action when select the number of three ai bots
     */
    @FXML
    public void handleThreeEnemies(ActionEvent event) {
        enemy2.setVisible(true);
        enemy3.setVisible(true);
    }

    /**Handle the action of pressing create button which will go to lobby.fxml
     * @throws OutOfBoundException the exception which alerts the invalid selection of total number of players and ai bots
     */
    @FXML
    public void handleCreateBtn() throws OutOfBoundException {
        //get selected properties

        //ai enemy number
        enemy_num = (int) num_group.getSelectedToggle().getUserData();
        //player number
        player_num = (int) num_player.getSelectedToggle().getUserData();
        //maximum total players is 4

        while (true) {
            if ((enemy_num + player_num) > 4) {
                alert.setVisible(true);
                throw new OutOfBoundException("The number of maximum player is 4 ");
            } else {
                selected_mode = String.valueOf(mode.getSelectedToggle().getUserData());
                System.out.println("REACH HERE");
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

                String fxmlPath = "../fxmls/ip_host.fxml";
                String stageTitle = "Host a Game";
                String fileException = "IP Host";
                FxmlLoader loader = new FxmlLoader(fxmlPath, stage, stageTitle, fileException, audioManager);

            }
        }
    }

    /***Handle the action of pressing back button which will go back to online_mode.fxml
     */

    @FXML
    public void handleBackBtn() {
        //select mode
        String fxmlPath = "../fxmls/online_mode.fxml";
        String stageTitle = "Online Configuration";
        String fileException = "Online Setup";
        FxmlLoader loader = new FxmlLoader(fxmlPath, stage, stageTitle, fileException, audioManager);
    }

    /** Initialise the set up of radio buttons
     * @param location  url location
     * @param resources resource bundled
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alert.setVisible(false);

        ToggleGroupSetUp.setToggleGroup(num_player, player_1, player_2, player_3, player_4);
        ToggleGroupSetUp.setToggleGroup(num_group, num_1, num_2, num_3);
        ToggleGroupSetUp.setToggleGroup(diff_1, easy_1, normal_1, hard_1);
        ToggleGroupSetUp.setToggleGroup(diff_2, easy_2, normal_2, hard_2);
        ToggleGroupSetUp.setToggleGroup(diff_3, easy_3, normal_3, hard_3);
        ToggleGroupSetUp.setToggleGroup(mode, life_mode, time_mode);


        player_1.setUserData(1);
        player_2.setUserData(2);
        player_3.setUserData(3);
        player_4.setUserData(4);
        ToggleGroupSetUp.setUserData("Easy", easy_1, easy_2, easy_3);
        ToggleGroupSetUp.setUserData("Normal", normal_1, normal_2, normal_3);
        ToggleGroupSetUp.setUserData("Hard", hard_1, hard_2, hard_3);

        num_1.setUserData(1);
        num_2.setUserData(2);
        num_3.setUserData(3);

        life_mode.setUserData("life_based");
        time_mode.setUserData("time_based");
    }
}
