package graphics.userInterface.controllers;

import client.GameClient;

import engine.model.generator.GameStateGenerator;
import client.ClientGameState;
import server.GameServer;
import server.ServerGameState;
import server.ServerGameStateGenerator;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import networking.Constants;

//For local_setup scene
public class HostController extends UIController{
	
	@FXML
	TextField ip_host;

    private ClientGameState gameState;
    private ServerGameState serverState;

    // directly go to local mode map
    @FXML
    public void handleStartBtn(ActionEvent event){
        // create game rules
        // todo make this configurable
        serverState = ServerGameStateGenerator.createEmptyState();
        gameState = GameStateGenerator.createEmptyState();

        try {
            // Create server
            String addr = ip_host.getText();

            if(addr.equals("") && !addr.isEmpty()) {
                System.out.println("Invalid ip address!");
            		//todo user needs to be asked to enter a valid ip adddress
            }else {
	            Constants.SERVER_ADDRESS = addr;
	            //loading lobby
	            String fxmlPath ="../fxmls/lobby.fxml";
	            String stageTitle ="Game Lobby";
	            String fileException ="Game Lobby";
	            FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);

	            LobbyController controller = (LobbyController) loader.getController();
	            controller.setIp(addr);
	            
	            GameClient gameBoard = null;
                try {
                    gameBoard = new GameClient(stage, gameState, addr, audioManager);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }

               GameServer server = new GameServer(serverState);
               controller.setGameClient(gameBoard);

                server.setLobbyController(controller);
                server.start();

               
                //Scene scene = gameBoard.getScene();
                //todo add gameclient properly
                //gameBoard.startNetwork();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void handleBackBtn(ActionEvent event){
        String fxmlPath ="../fxmls/online_setup.fxml";
        String stageTitle ="Online configuration";
        String fileException ="Online Setup";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }


}

