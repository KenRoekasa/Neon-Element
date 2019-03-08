package graphics.userInterface.controllers;

import client.GameClient;
import client.ClientGameState;
import client.GameStateGenerator;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

//For local_setup scene
public class JoinController extends UIController {
    @FXML
    Label ok,back;

    @FXML
    public TextField ip;

    private ClientGameState gameState;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize() {
        // TODO: store/retrieve last connected IP
        ip.setText("127.0.0.1");
    }

    @FXML
    public void handleOkBtn(){
        // create game rules
        // todo make this configurable
            gameState = GameStateGenerator.createEmptyState();
        //g.getPlayer().getHealth();
        try {
            String addr = ip.getText();
            GameClient gameBoard = new GameClient(stage, gameState, addr);
            Scene scene = gameBoard.getScene();
            gameBoard.startNetwork();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void handleBackBtn(){
        String fxmlPath ="../fxmls/online_mode.fxml";
        String stageTitle ="Online Mode";
        String fileException ="Online Mode";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        back.setTextFill(outline);
        ok.setTextFill(outline);
        back.setEffect(blend);
        ok.setEffect(blend);
    }
}

