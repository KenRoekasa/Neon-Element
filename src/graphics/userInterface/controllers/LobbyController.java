package graphics.userInterface.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for lobby.fxml for setting game lobby
 */
public class LobbyController extends UIController{
    /**
     * Texts of the connections
     */
    @FXML
    Text conn_1,conn_2,conn_3,conn_4;

    /**
     * List for storing all texts
     */
    private ArrayList<Text> textList = new ArrayList<>();

    /**
     * String property of connection1
     */
    private StringProperty conn1Property = new SimpleStringProperty();
    /**
     * String property of connection2
     */
    private StringProperty conn2Property = new SimpleStringProperty();
    /**
     * String property of connection3
     */
    private StringProperty conn3Property = new SimpleStringProperty();
    /**
     * String property of connection4
     */
    private StringProperty conn4Property = new SimpleStringProperty();

    /**
     * The string template of show that user connect to the game
     */
    private final static String TEMP = "Player %d connects ";


    /**Handle the action of pressing start button which will go to game board
     */
    @FXML
    public void handleStartBtn(){
        //TODO:create a game
    }

    /** When player connects to the game, let the controller knows
     * @param playerID player's id
     * @param connection
     */
    //todo once the player connects, call this function
    public void connect(int playerID,Text connection){
        connection.setText(String.format(TEMP,playerID));
        connection.setVisible(true);
    }

    /** Initialise the controller setting, implement the user interface value binding
     * @param location  url location
     * @param resources resource bundled
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        textList.add(conn_1);
        textList.add(conn_2);
        textList.add(conn_3);
        textList.add(conn_4);
        for(Text text:textList){
            text.setVisible(false);
        }

        conn_1.textProperty().bind(conn1Property);
        conn_2.textProperty().bind(conn2Property);
        conn_3.textProperty().bind(conn3Property);
        conn_4.textProperty().bind(conn4Property);
    }


}
