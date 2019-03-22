package server;

import engine.controller.GameTypeHandler;
import engine.physics.DeltaTime;
import engine.physics.PhysicsController;
import graphics.userInterface.controllers.LobbyController;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import javafx.geometry.Point2D;
import networking.Constants;
import networking.server.ConnectedPlayers;
import javafx.scene.transform.Rotate;
import networking.server.ServerNetwork;
import server.controllers.PowerUpController;

public class GameServer extends Thread {

    private ServerGameState gameState;
    private ServerNetwork network;
    private PhysicsController physicsController;
    private LobbyController lobbyController;
    private boolean isGameStart = false;

    public LobbyController getLobbyController() {
        return lobbyController;
    }

    public void setLobbyController(LobbyController lobbyController) {
        this.lobbyController = lobbyController;
    }

    private boolean running;

    private int expectedPlayersToJoin = Constants.NUM_PLAYER;

    public GameServer(ServerGameState gameState) {
        this.gameState = gameState;
        this.network = new ServerNetwork(this.gameState);
        this.physicsController = new PhysicsController(gameState);
    }

    public void run() {
        this.network.start();
        System.out.println("Constants in server"+Constants.NUM_PLAYER);

        System.out.println("Game started.");

        Thread powerUpController = new Thread(new PowerUpController(gameState, this.network.getDispatcher()));
        powerUpController.start();

        this.running = true;
        long lastTime = System.nanoTime();
        while (this.running) {
            // Server logic
            if (!isGameStart) {
                this.waitForPlayersToConnect();
                System.out.println("Waiting for connection!");
            } else {

                physicsController.clientLoop();

                this.running = GameTypeHandler.checkRunning(gameState);

                Thread.yield();
                this.sendLocations();

                //calculate deltaTime
                long time = System.nanoTime();
                DeltaTime.deltaTime = (int) ((time - lastTime) / 1000000);
                lastTime = time;

                try {
                    Thread.sleep(25); // Every second
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        this.network.close();
    }

    private void waitForPlayersToConnect() {
        ConnectedPlayers connectedPlayers = this.network.getConnectedPlayers();
        //todo can't get the connected player : how and where player added

        if(connectedPlayers != null) {
            lobbyController.showConnections(connectedPlayers.getPlayerIds());
        }

        // Wait for enough players to start the game
        /*
         * while (connectedPlayers.count() < expectedPlayersToJoin) {
         * lobbyController.update(); }
         */
        if (lobbyController.isStartGame()) {
            System.out.println("Game server start the game!");
            // Start the game
            connectedPlayers.assignStartingLocations(gameState.getMap().getWidth(), gameState.getMap().getHeight());
            this.gameState.getScoreBoard().initialise(this.gameState.getAllPlayers());
            this.network.getDispatcher().broadcastGameState();

            this.gameState.setStarted(true);
            this.network.getDispatcher().broadcastGameStarted();
            isGameStart = true;

        }
    }

    private void sendLocations() {
        synchronized (gameState.getAllPlayers()) {
            for (Player p : gameState.getAllPlayers()) {
                Point2D location = p.getLocation();
                Rotate playerAngle = p.getPlayerAngle();
                double x = location.getX();
                double y = location.getY();

                this.network.getDispatcher().broadcastLocationState(p.getId(), x, y, playerAngle.getAngle());
            }
        }
    }

    public ServerNetwork getNetwork() {
        return network;
    }

}
