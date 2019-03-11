package graphics.userInterface.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LobbyController extends UIController{
    @FXML
    Text conn_1,conn_2,conn_3,conn_4;

    private ArrayList<Text> textList = new ArrayList<>();

    private StringProperty conn1Property = new SimpleStringProperty();
    private StringProperty conn2Property = new SimpleStringProperty();
    private StringProperty conn3Property = new SimpleStringProperty();
    private StringProperty conn4Property = new SimpleStringProperty();

    private final static String TEMP = "Player %d connects ";


    @FXML
    public void handleStartBtn(){
        //TODO:create a game

    }

    //once the player connects, call this function
    public void connect(int playerID,Text connection){
        connection.setText(String.format(TEMP,playerID));
        connection.setVisible(true);
    }

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
