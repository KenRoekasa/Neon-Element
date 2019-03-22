package server;

import java.util.ArrayList;
import java.util.Iterator;

import engine.controller.GameTypeHandler;
import engine.physics.CollisionDetector;
import engine.physics.PhysicsController;
import graphics.userInterface.controllers.LobbyController;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.entities.PowerUp;
import engine.model.enums.Action;
import engine.model.enums.ObjectType;
import javafx.geometry.Point2D;
import networking.Constants;
import networking.server.ConnectedPlayers;
import javafx.scene.transform.Rotate;
import networking.server.ServerNetwork;
import server.controllers.PowerUpController;

import java.util.ArrayList;
import java.util.Iterator;

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

	private void doCollisionDetection() {

		ArrayList<PhysicsObject> objects = gameState.getObjects();
		synchronized (objects) {
			// Collision detection code
			Player projectedPlayer = new Player(ObjectType.PLAYER);
			for (Iterator<Player> playerItr = gameState.getAllPlayers().iterator(); playerItr.hasNext();) {
				Player player = playerItr.next();
				for (Iterator<PhysicsObject> itr = objects.iterator(); itr.hasNext();) {
					PhysicsObject e = itr.next();
					// Check if the moving in a certain direction will cause a collision
					// The player has collided with e do something
					if (e.getTag() == ObjectType.POWERUP) {
						if (CollisionDetector.checkCollision(player, e)) {
							PowerUp powerUp = (PowerUp) e;
							powerUp.activatePowerUp(player);
							// remove power up from objects array list
							itr.remove();

						}
					} else {
						double x = player.getLocation().getX();
						double y = player.getLocation().getY();
						float movementSpeed = player.getMovementSpeed();
						Point2D checkUp = new Point2D(x - movementSpeed, y - movementSpeed);
						Point2D checkDown = new Point2D(x + movementSpeed, y + movementSpeed);
						Point2D checkLeft = new Point2D(x - movementSpeed, y + movementSpeed);
						Point2D checkRight = new Point2D(x + movementSpeed, y - movementSpeed);
						Point2D checkUpCart = new Point2D(x, y - movementSpeed);
						Point2D checkDownCart = new Point2D(x, y + movementSpeed);
						Point2D checkLeftCart = new Point2D(x - movementSpeed, y);
						Point2D checkRightCart = new Point2D(x + movementSpeed, y);
						Point2D[] projectedLocations = { checkUp, checkDown, checkLeft, checkRight, checkUpCart,
								checkDownCart, checkLeftCart, checkRightCart };

						// System.out.println(player.getCharacterDirection());

						switch (player.getCharacterDirection()) {
						case UP:
							projectedPlayer.setLocation(checkUp);
							if (CollisionDetector.checkCollision(projectedPlayer, e)) {
							}

							break;
						case DOWN:
							projectedPlayer.setLocation(checkDown);
							if ((CollisionDetector.checkCollision(projectedPlayer, e))) {
							}
							break;
						case LEFT:
							projectedPlayer.setLocation(checkLeft);
							if ((CollisionDetector.checkCollision(projectedPlayer, e))) {

							}

							break;
						case UPCART:
							projectedPlayer.setLocation(checkUpCart);
							if ((CollisionDetector.checkCollision(projectedPlayer, e))) {
							}
							break;
						case DOWNCART:
							projectedPlayer.setLocation(checkDownCart);
							if ((CollisionDetector.checkCollision(projectedPlayer, e))) {
							}
							break;
						case LEFTCART:
							projectedPlayer.setLocation(checkLeftCart);
							if ((CollisionDetector.checkCollision(projectedPlayer, e))) {
							}
							break;
						case RIGHTCART:
							projectedPlayer.setLocation(checkRightCart);
							if ((CollisionDetector.checkCollision(projectedPlayer, e))) {
							}
							break;
						case RIGHT:
							projectedPlayer.setLocation(checkRight);
							if ((CollisionDetector.checkCollision(projectedPlayer, e))) {
							}
							break;
						}
					}
				}

				// Loop through all enemies to detect hit detection
				ArrayList<Player> enemies = gameState.getOtherPlayers(player);
				synchronized (enemies) {
					for (Iterator<Player> itr = enemies.iterator(); itr.hasNext();) {
						PhysicsObject e = itr.next();
						// Attack Collision
						// if player is light attacking
						if (player.getCurrentAction() == Action.LIGHT) {
							if (CollisionDetector.checkCollision(player.getAttackHitbox(), e.getBounds())) {
								// e takes damage
								// this will have to change due to Player being other controlled player when
								// Enemy is when the player is an engine.ai
								Player enemy = (Player) e;
								// TODO: For now its takes 3 damage, change later
								enemy.removeHealth(3);
								player.setCurrentAction(Action.IDLE);
								// System.out.println("hit");
								// Sends to server
							}

						}
						if (player.getCurrentAction() == Action.HEAVY) {
							if (CollisionDetector.checkCollision(player.getHeavyAttackHitbox(), e.getBounds())) {

								// e takes damage
								Player enemy = (Player) e;
								// TODO: For now its takes 10 damage, change later
								enemy.removeHealth(10);
								player.setCurrentAction(Action.IDLE);
								// System.out.println("heavy hit");
								// Sends to server
							}
						}
					}
				}
			}
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
