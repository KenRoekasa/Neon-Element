package server;

import engine.controller.GameTypeHandler;
import engine.controller.RespawnController;
import engine.physics.DeltaTime;
import engine.physics.PhysicsController;
import graphics.userInterface.controllers.LobbyHostController;
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

	private boolean running;

	public GameServer(ServerGameState gameState) {
		this.gameState = gameState;
		this.network = new ServerNetwork(this.gameState);
		this.physicsController = new PhysicsController(gameState);
	}

	public void run() {
		this.network.start();

		PowerUpController puController = new PowerUpController(gameState, this.network.getDispatcher());
		RespawnController resController = new RespawnController(gameState);

        this.running = true;
        long lastTime = System.nanoTime();
		while (this.running) {
			// Server logic

			if (!this.gameState.isStarted()) {

				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				this.waitForPlayersToConnect();
			} else {

				physicsController.clientLoop();
				puController.serverUpdate();
				resController.update();

				this.running = GameTypeHandler.checkRunning(gameState);

				Thread.yield();
                this.sendLocations();
                this.sendHealthUpdates();

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

		this.network.getDispatcher().broadcastGameEnded();

		this.network.close();
	}

	private void waitForPlayersToConnect() {
		ConnectedPlayers connectedPlayers = this.network.getConnectedPlayers();
		//todo can't get the connected player : how and where player added

        // once players are all connected, start the game
        if(connectedPlayers != null && connectedPlayers.count() == Constants.NUM_PLAYER) {
			// Start the game
			connectedPlayers.assignStartingLocations(gameState.getMap().getWidth(), gameState.getMap().getHeight());
			this.gameState.getScoreBoard().initialise(this.gameState.getAllPlayers());
			this.network.getDispatcher().broadcastGameState();

			this.gameState.setStarted(true);
			this.network.getDispatcher().broadcastGameStarted();

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

	private void sendHealthUpdates() {
		synchronized (gameState.getAllPlayers()) {
			for (Player p : gameState.getAllPlayers()) {
				float playerHealth = p.getHealth();

				this.network.getDispatcher().broadcastHealthState(p.getId(),playerHealth);
			}
		}
	}

	public ServerNetwork getNetwork() {
		return network;
	}
}
