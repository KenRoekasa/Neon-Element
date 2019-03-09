package graphics.userInterface.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyController extends UIController{
    @FXML
    Text conn_1,conn_2,conn_3,conn_4;

    StringProperty conn1Property = new SimpleStringProperty();
    StringProperty conn2Property = new SimpleStringProperty();
    StringProperty conn3Property = new SimpleStringProperty();
    StringProperty conn4Property = new SimpleStringProperty();

    public void handleStartBtn(){


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn_1.textProperty().bind(conn1Property);
        conn_2.textProperty().bind(conn2Property);
        conn_3.textProperty().bind(conn3Property);
        conn_4.textProperty().bind(conn4Property);
    }


}
