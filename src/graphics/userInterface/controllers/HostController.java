package graphics.userInterface.controllers;

import client.GameClient;

import engine.model.generator.GameStateGenerator;
import client.ClientGameState;
import server.ServerGameState;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

//For local_setup scene
public class HostController extends UIController{
    @FXML
    Label back,start;
    private ClientGameState gameState;
    private ServerGameState serverState;

    // directly go to local mode map
    @FXML
    public void handleStartBtn(ActionEvent event){
        // create game rules
        // todo make this configurable
        gameState = GameStateGenerator.createEmptyState();
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

            String addr = "localhost";
            GameClient gameBoard = new GameClient(stage, gameState, addr, audioManager);
            Scene scene = gameBoard.getScene();
            gameBoard.startNetwork();
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

