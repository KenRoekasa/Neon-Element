package graphics.userInterface.controllers;

import client.ClientGameState;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for ip_host.fxml
 */
public class HostController extends UIController{
    /**
     * The GameState of client side
     */
    private ClientGameState gameState;
    /**
     * The host IP address of the game
     */
    @FXML
    public TextField ip;
    private String iP;
    //TODO: call this function in the networking part


    /** Set the ip address to this controller
     * @param iP the string of ip address of current game
     */
    public void setiP(String iP) {
        this.iP = iP;
        ip_value.set(iP);
    }

    /**
     * The text which shows the current ip address on the user interface
     */
    @FXML
    Text ip_address;
    /**
     * The string property of the ip_address
     */
    StringProperty ip_value = new SimpleStringProperty();

    /** Handle the action of pressing start button which will direct to lobby.fxml
     */
    public void handleStartBtn(){
        String fxmlPath ="../fxmls/lobby.fxml";
        String stageTitle ="Game Lobby";
        String fileException ="lobby";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }

    /**Handle the action of pressing back button which will direct to online_setup.fxml
     */
    @FXML
    public void handleBackBtn(){
        String fxmlPath ="../fxmls/online_setup.fxml";
        String stageTitle ="Online configuration";
        String fileException ="Online Setup";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }
}

