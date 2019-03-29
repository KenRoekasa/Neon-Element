package graphics.userInterface.controllers;

import client.ClientGameState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for online_setup.fxml
 */
public class OnlineSetUpController extends UIController {
    /**
     * Radio buttons of different number of player and different number of ai bots
     */
    @FXML
    public RadioButton player_2, player_3, player_4;


    /**
     * Radio buttons of modes
     */
    @FXML
    public RadioButton FirstToXKills, Timed, Hill;


    /**
     * Back button
     */
    @FXML
    public Button back_btn;

    /**
     * The number of players
     */
    private int player_num;

    /**
     * The selectd mode for the game
     */
    private String selected_mode;

    /**
     * The list which contains the selected difficulties of ai bots
     */
    private ArrayList<String> enemyTypes = new ArrayList<>();

    /**
     * The toggle group of number of players
     */
    final ToggleGroup num_player = new ToggleGroup();

    /**
     * The toggle group of game mode
     */
    final ToggleGroup mode = new ToggleGroup();

    /**
     * Handle the action of pressing create button which will go to lobby_host.fxml
     */
    @FXML
    public void handleCreateBtn(){
        //get selected properties
        //player number
        player_num = (int) num_player.getSelectedToggle().getUserData();
        //maximum total players is 4

        selected_mode = String.valueOf(mode.getSelectedToggle().getUserData());
        String fxmlPath = "../fxmls/ip_host.fxml";
        String stageTitle = "Host a Game";
        String fileException = "IP Host";

        FxmlLoader loader = new FxmlLoader(fxmlPath, stage, stageTitle, fileException, audioManager);
        ((HostController) loader.getController()).setGameAttributes(player_num, selected_mode);
    }


    /**Handle the action of pressing back button which will go back to online_mode.fxml
     */
    @FXML
    public void handleBackBtn() {
        //select mode
        String fxmlPath = "../fxmls/online_mode.fxml";
        String stageTitle = "Online Configuration";
        String fileException = "Online Setup";
        FxmlLoader loader = new FxmlLoader(fxmlPath, stage, stageTitle, fileException, audioManager);
    }

    /**
     * Initialise the set up of radio buttons
     *
     * @param location  url location
     * @param resources resource bundled
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ToggleGroupSetUp.setToggleGroup(num_player, player_2, player_3, player_4);
        ToggleGroupSetUp.setToggleGroup(mode, FirstToXKills, Hill, Timed);

        player_2.setUserData(2);
        player_3.setUserData(3);
        player_4.setUserData(4);

        FirstToXKills.setUserData("FirstToXKills");
        Hill.setUserData("Hill");
        Timed.setUserData("Timed");


    }
}
