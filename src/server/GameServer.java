package server;

import client.GameClient;
import engine.controller.GameTypeHandler;
import engine.controller.RespawnController;
import engine.physics.DeltaTime;
import engine.physics.PhysicsController;
import engine.entities.Player;
import javafx.geometry.Point2D;
import networking.server.ConnectedPlayers;
import javafx.scene.transform.Rotate;
import networking.server.ServerNetwork;
import engine.controller.PowerUpController;

public class GameServer extends Thread {

	private ServerGameState gameState;
	private ServerNetwork network;

	private boolean running;

	public GameServer(ServerGameState gameState) {
		this.gameState = gameState;
		this.network = new ServerNetwork(this.gameState);
	}

	public void run() {
		this.network.start();

		PowerUpController puController = new PowerUpController(gameState, this.network.getDispatcher());
		RespawnController resController = new RespawnController(gameState, this.network.getDispatcher());
		PhysicsController physicsController = new PhysicsController(gameState,this.network.getDispatcher());

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

				physicsController.serverLoop();

				puController.serverUpdate();

				resController.update();

				this.running = GameTypeHandler.checkRunning(gameState);

				Thread.yield();
				this.sendLocations();
				this.sendHealthUpdates();

				GameClient.timeElapsed += DeltaTime.deltaTime;
                //calculate deltaTime
                long time = System.nanoTime();
                DeltaTime.deltaTime = (int) ((time - lastTime) / 1000000);
                lastTime = time;

				try {
					Thread.sleep(15); // Every second
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
        if(connectedPlayers != null && connectedPlayers.count() == gameState.getNumPlayers()) {
			// Start the game
			connectedPlayers.assignStartingLocations(gameState.getMap().getWidth(), gameState.getMap().getHeight());
			this.gameState.getScoreBoard().initialise(this.gameState.getAllPlayers());
			this.network.getDispatcher().broadcastGameState();

			if (connectedPlayers.allHaveInitialGameState()) {
				this.gameState.setStarted(true);
				this.network.getDispatcher().broadcastGameStarted();
				GameClient.timeElapsed = 0;

			}
		}
	}

	private void sendLocations() {
		synchronized (gameState.getAllPlayers()) {
			for (Player p : gameState.getAllPlayers()) {
				if (p.isAlive()) {
					Point2D location = p.getLocation();
					Rotate playerAngle = p.getPlayerAngle();
					double x = location.getX();
					double y = location.getY();

					this.network.getDispatcher().broadcastLocationState(p.getId(), x, y, playerAngle.getAngle());
				}
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
