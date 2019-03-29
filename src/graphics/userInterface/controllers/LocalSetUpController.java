package graphics.userInterface.controllers;

import client.GameClient;
import client.ClientGameState;
import engine.model.GameType;
import engine.model.generator.GameStateGenerator;

import client.audiomanager.Music;
import javafx.fxml.FXML;

import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


/**
 * Controller of local_setup.fxml
 */
public class LocalSetUpController extends UIController {
    /**
     * The game state of current game
     */
    private ClientGameState gameState;
    /**
     * Radio buttons of one ai bot,two ai bots,three ai bots
     */
    @FXML
    public RadioButton num_1, num_2, num_3;
    /**
     * Radio buttons for three different difficulties of three different number of ai bots
     */
    @FXML
    public RadioButton easy_1, easy_2, easy_3, normal_1, normal_2, normal_3, hard_1, hard_2, hard_3;
    /**
     * Radio buttons of time mode game and life mode game
     */
    @FXML
    public RadioButton FirstToXKills,Timed,Hill,Regicide;

    /**
     * Grid Panes of the second ai bot setting and the third ai bot setting
     */
    @FXML
    GridPane enemy2, enemy3;
    /**
     * Toggle group of the number of ai bots
     */
    final ToggleGroup num_group = new ToggleGroup();
    /**
     * Toggle group of the difficulties for the first ai bot
     */
    final ToggleGroup diff_1 = new ToggleGroup();
    /**
     * Toggle group of the difficulties for the second ai bot
     */
    final ToggleGroup diff_2 = new ToggleGroup();
    /**
     * Toggle group of the difficulties for the third ai bot
     */
    final ToggleGroup diff_3 = new ToggleGroup();
    /**
     * Toggle group of game modes
     */
    final ToggleGroup mode = new ToggleGroup();

    /**
     * The number of ai bots
     */
    private int enemy_num;
    /**
     * The difficulty selected for the first ai bot
     */
    private String enemy_1;
    /**
     * The difficulty selected for the second ai bot
     */
    private String enemy_2;
    /**
     * The difficulty selected for the third ai bot
     */
    private String enemy_3;

    /**
     * The mode selected for the game
     */
    private GameType.Type selected_mode;

    /**
     * A list for storing the selected difficulties of the ai bots
     */
    ArrayList<String> enemyTypes = new ArrayList<>();


    /** Get the selected game mode
     * @return selected game mode
     */

    /** Handle the action when select the number of one ai bot
     */
    // directly go to local mode map
    @FXML
    public void handleOneEnemy() {
        enemy2.setVisible(false);
        enemy3.setVisible(false);
    }

    /** Handle the action when select the number of two ai bots
     */
    @FXML
    public void handleTwoEnemies() {
        enemy2.setVisible(true);
        enemy3.setVisible(false);
    }

    /** Handle the action when select the number of three ai bots
     */
    @FXML
    public void handleThreeEnemies() {
        enemy2.setVisible(true);
        enemy3.setVisible(true);
    }


    /** Handle the action of pressing the start button
     */
    @FXML
    public void handleStartBtn() {
        //get selected properties
        enemy_num = (int) num_group.getSelectedToggle().getUserData();

        selected_mode = (GameType.Type)mode.getSelectedToggle().getUserData();

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
        gameState = GameStateGenerator.createDemoGamestateSample(enemy_num,enemyTypes,selected_mode);
        //g.getPlayer().getHealth();
        try {
            GameClient gameBoard = new GameClient(stage, gameState, audioManager);
            gameBoard.initialiseGame();
            gameState.start();
            Scene scene = gameBoard.getScene();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**Handle the action of pressing back button which will direct to mode.fxml
     */
    @FXML
    public void handleBackBtn() {
        String fxmlPath ="../fxmls/mode.fxml";
        String stageTitle ="Mode" ;
        String fileException ="Mode";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }

    /** Initialise the set up of toggle groups
     * @param location  url location
     * @param resources resource bundled
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ToggleGroupSetUp.setToggleGroup(num_group,num_1,num_2,num_3);
        ToggleGroupSetUp.setToggleGroup(diff_1,easy_1,normal_1,hard_1);
        ToggleGroupSetUp.setToggleGroup(diff_2,easy_2,normal_2,hard_2);
        ToggleGroupSetUp.setToggleGroup(diff_3,easy_3,normal_3,hard_3);
        ToggleGroupSetUp.setToggleGroup(mode,FirstToXKills,Hill,Timed,Regicide);
        
        ToggleGroupSetUp.setUserData("Easy",easy_1,easy_2,easy_3);
        ToggleGroupSetUp.setUserData("Normal",normal_1,normal_2,normal_3);
        ToggleGroupSetUp.setUserData("Hard",hard_1,hard_2,hard_3);

        num_1.setUserData(1);
        num_2.setUserData(2);
        num_3.setUserData(3);

        FirstToXKills.setUserData(GameType.Type.FirstToXKills);
        Hill.setUserData(GameType.Type.Hill);
        Timed.setUserData(GameType.Type.Timed);
        Regicide.setUserData(GameType.Type.Regicide);

    }
}










