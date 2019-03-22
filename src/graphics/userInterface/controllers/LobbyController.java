package graphics.userInterface.controllers;

import client.GameClient;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import networking.Constants;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for lobby.fxml for setting game lobby
 */
public class LobbyController extends UIController{

    public void setGameClient(GameClient gameClient) {
        this.gameClient = gameClient;
    }

    private GameClient gameClient;
	private boolean startGame = false;


    public boolean isStartGame() {
		return startGame;
	}

	public void setStartGame(boolean startGame) {
		this.startGame = startGame;
	}

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


    /** When player connects to the game, let the controller knows
     * @param playerID player's id
     * @param connection
     */
    //todo once the player connects, call this function
    public void connect(int playerID,Text text,StringProperty connection){
        connection.set(String.format(TEMP,playerID));
        text.setVisible(true);
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

    public void showConnections(ArrayList<Integer> playerIds){
        if(!gameClient.isNetworked){
            gameClient.startNetwork();
            gameClient.isNetworked = true ;
        }
        int size = playerIds.size();
        System.out.println("Connected player's number : "+size);
        switch (size){
            case 1:
                connect(playerIds.get(0),conn_1,conn1Property);
                break;
            case 2:
                connect(playerIds.get(0),conn_1,conn1Property);
                connect(playerIds.get(1),conn_2,conn2Property);
            case 3:
                connect(playerIds.get(0),conn_1,conn1Property);
                connect(playerIds.get(1),conn_2,conn2Property);
                connect(playerIds.get(2),conn_3,conn3Property);
            case 4:
                connect(playerIds.get(0),conn_1,conn1Property);
                connect(playerIds.get(1),conn_2,conn2Property);
                connect(playerIds.get(2),conn_3,conn3Property);
                connect(playerIds.get(3),conn_4,conn4Property);
        }
        //once players are all connected, start the game
        if(size == Constants.NUM_PLAYER) {
            startGame = true;
            startGame();
        }
    }

    public void startGame(){
        gameClient.initialiseGame();
    }

}
