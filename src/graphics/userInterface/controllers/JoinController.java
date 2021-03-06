package graphics.userInterface.controllers;

import client.GameClient;
import client.ClientGameState;
import engine.model.generator.GameStateGenerator;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import networking.Constants;


/**
 * Controller for join.fxml
 */
public class JoinController extends UIController {
    /**
     * Current game state
     */
    private ClientGameState gameState;
    /**
     * The text field for entering ip address
     */
    @FXML
    Label back;

    @FXML
    public TextField ip;

    /**Handle the action of pressing start button which will go to game board
     */
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize() {
        // TODO: store/retrieve last connected IP
        ip.setText("127.0.0.1");
    }

    /**
     * Handle the action when press the start button which go to game lobby
     */
    @FXML
    public void handleStartBtn(){
        // create game rules
        // todo make this configurable
            gameState = GameStateGenerator.createEmptyState();
        //g.getPlayer().getHealth();
        try {

           String addr = ip.getText();
          //  String addr =addr ;

            String fxmlPath ="../fxmls/lobby_join.fxml";
            String stageTitle ="Game Lobby";
            String fileException ="Game Lobby";
            FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
            LobbyJoinController controller = (LobbyJoinController) loader.getController();



            System.out.println("------------Server address:"+addr+" serCons: ");

            //todo figure out the server listining port
            //THIS IS HAPPENING BEFORE IT CAN RECEIVE GAMESTATE FROM SERVER
            GameClient gameBoard = new GameClient(stage, gameState, addr, audioManager);
            controller.setGameClient(gameBoard);
            //Scene scene = gameBoard.getScene();
            gameBoard.startNetwork();
            (new Thread(new Runnable() {
                @Override
                public void run() {
                    while(!gameState.getRunning()){
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    controller.startGame();
                }
            })).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *Handle the action of pressing back button which will be back to online_mode.fxml
     */
    @FXML
    public void handleBackBtn(){
        String fxmlPath ="../fxmls/online_mode.fxml";
        String stageTitle ="Online Mode";
        String fileException ="Online Mode";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }



}

