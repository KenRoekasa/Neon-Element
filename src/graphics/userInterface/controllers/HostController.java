package graphics.userInterface.controllers;

import client.ClientGameState;
import client.GameClient;
import engine.model.generator.GameStateGenerator;
import graphics.userInterface.LobbyThread;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.lang.reflect.Method;
import java.net.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for ip_host.fxml
 */
public class HostController extends UIController{

	@FXML
	TextField ip_host;

    private ClientGameState gameState;
    private int playerNum;
    private int AiNum;
    private ArrayList<String> AiType;
    private String gameType;


    /**
     * The host IP address of the game
     */
    @FXML
    public void handleStartBtn(ActionEvent event) {
        // create game rules
        // todo make this configurable
         gameState = GameStateGenerator.createEmptyState();

        try {
            // Create server
            String addr = ip_host.getText();

            if (addr.equals("") && !addr.isEmpty()) {
                System.out.println("Invalid ip address!");
                //todo user needs to be asked to enter a valid ip adddress
            } else {
                //loading lobby
                String fxmlPath = "../fxmls/lobby_host.fxml";
                String stageTitle = "Game Lobby";
                String fileException = "Game Lobby";
                FxmlLoader loader = new FxmlLoader(fxmlPath, stage, stageTitle, fileException, audioManager);

                GameClient gameBoard = null;
                try {
                    gameBoard = new GameClient(stage, gameState, addr, audioManager);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //Gets lobby host controller
                AbstractLobbyController controller = (LobbyHostController) loader.getController();
                controller.setGameClient(gameBoard);

                try {
                    // Create server
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                // create the custom class loader
                                ClassLoader cl = new URLClassLoader(new URL[0]);

                                // load the class
                                Class<?> classToLoad = cl.loadClass("server.ServerLauncher");

                                String[] args = new String[]{String.valueOf(playerNum), String.valueOf(AiNum), String.join(",", AiType), gameType};


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

                gameBoard.startNetwork();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * The text which shows the current ip address on the user interface
     */
    @FXML
    Text ip_address;
    /**
     * The string property of the ip_address
     */
    StringProperty ip_value = new SimpleStringProperty();

    /** Handle the action of pressing start button which will direct to lobby.fxml
     */
    public void handleStartBtn(){
        String fxmlPath ="../fxmls/lobby.fxml";
        String stageTitle ="Game Lobby";
        String fileException ="lobby";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }

    /**Handle the action of pressing back button which will direct to online_setup.fxml
     */
    @FXML
    public void handleBackBtn(){
        String fxmlPath ="../fxmls/online_setup.fxml";
        String stageTitle ="Online configuration";
        String fileException ="Online Setup";
        FxmlLoader loader = new FxmlLoader(fxmlPath,stage,stageTitle,fileException, audioManager);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip_host.setText(socket.getLocalAddress().getHostAddress());
        } catch (SocketException | UnknownHostException e) {
            ip_host.setText("Failed to auto detect IP");
        }

    }

    public void setGameAttributes(int playerNum, int AiNum, ArrayList<String> AiType, String type) {
        this.playerNum = playerNum;
        this.AiNum = AiNum;
        this.AiType = AiType;
        this.gameType = type;
    }
}

