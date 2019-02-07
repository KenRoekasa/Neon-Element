package controllers;

import client.GameState;
import client.GameStateGenerator;
import client.ClientBoard;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;

/* Menu buttons:
1. Play
2. Options
3. Help
4. Exit
*/

public class MenuController implements Initializable{
    private Stage stage;
    private GameState gameState;
    private PlayerModel playerModel;
    @FXML
    private Text health;
    @FXML
    private Text speed;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void handleBTNPlay(ActionEvent actionEvent) {

        // create game rules
        // todo make this configurable
            gameState = GameStateGenerator.createDemoGamestate();
            playerModel = new PlayerModel();
        //g.getPlayer().getHealth();
        try {
            ClientBoard gameBoard = new ClientBoard(stage, gameState, playerModel);
            Scene scene = gameBoard.getScene();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @FXML
    public void handleBTNOptions(ActionEvent actionEvent){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../userInterface/Options.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Options");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void handleBTNHelp(ActionEvent actionEvent){

    }

    @FXML
    public void handleBTNExit(ActionEvent actionEvent){
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //bind model value to javafx text property
        //health.textProperty().bind(playerModel.healthProperty());
        //speed.textProperty().bind(playerModel.speedProperty());
    }

}
