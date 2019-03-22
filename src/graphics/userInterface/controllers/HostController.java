package graphics.userInterface.controllers;

import client.GameClient;

import engine.model.generator.GameStateGenerator;
import client.ClientGameState;
import graphics.userInterface.LobbyThread;
import server.GameServer;
import server.ServerGameState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import server.ServerGameStateGenerator;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

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
        gameState = GameStateGenerator.createEmptyState();
        serverState = ServerGameStateGenerator.createEmptyState();

        try {
            // Create server
            String addr = ip_host.getText();

            if(addr.equals("") && !addr.isEmpty()) {
                System.out.println("Invalid ip address!");
            		//todo user needs to be asked to enter a valid ip adddress
            }else {
	            //loading lobby
	            String fxmlPath ="../fxmls/lobby_host.fxml";
	            String stageTitle ="Game Lobby";
	            String fileException ="Game Lobby";
	            FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);

	            GameClient gameBoard = null;
                try {
                    gameBoard = new GameClient(stage, gameState, addr, audioManager);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                AbstractLobbyController controller = (AbstractLobbyController) loader.getController();
                controller.setGameClient(gameBoard);

                try {
                    // Create server
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                // create the custom class loader
                                ClassLoader cl = new URLClassLoader(new URL[0]);

                                // load the class
                                Class<?> classToLoad = cl.loadClass("networking.test.ManualTestServer");

                                String[] args = new String[0];

                                // get the main method
                                Method main = classToLoad.getMethod("main", args.getClass());

                                // and invoke it
                                main.invoke(null, (Object) args);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                LobbyThread lobbyThread = new LobbyThread(gameState, controller);
                lobbyThread.start();

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

