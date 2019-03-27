package networking.server;

import engine.entities.Player;
import engine.model.enums.ObjectType;
import javafx.scene.transform.Rotate;
import networking.packets.*;
import server.ServerGameState;

public class ServerNetworkHandler {

    private ServerGameState gameState;
    private ConnectedPlayers connectedPlayers;
    private ServerNetworkDispatcher dispatcher;

    protected ServerNetworkHandler(ServerGameState gameState, ConnectedPlayers connectedPlayers, ServerNetworkDispatcher dispatcher) {
        this.gameState = gameState;
        this.connectedPlayers = connectedPlayers;
        this.dispatcher = dispatcher;
    }

	public void receiveHello(HelloPacket packet) {
		// TODO - integrate and get these values from somewhere
		int players = this.gameState.getAllPlayers().size();
		int maxPlayers = this.gameState.getNumPlayers();

		Packet response = new HelloAckPacket(players, maxPlayers, this.gameState.getGameType());
		this.dispatcher.send(response, packet.getIpAddress(), packet.getPort());
	}

	public void receiveConnect(ConnectPacket packet) {
		boolean isStarted = this.gameState.isStarted();
		boolean hasSpace = this.gameState.getAllPlayers().size() < this.gameState.getNumPlayers();
		System.out.println("Does the game have space: " + hasSpace);
		System.out.println("has the game Started: " + isStarted);

		// Allow connection if the game has not started yet and we have space for more
		// players
		ConnectAckPacket.Status status;
		int playerId = 0;
		if (isStarted) {
			status = ConnectAckPacket.Status.ERR_GAME_STARTED;
		} else if (!hasSpace) {
			status = ConnectAckPacket.Status.ERR_MAX_PLAYERS;
		} else {
			status = ConnectAckPacket.Status.SUC_CONNECTED;

			Player player = new Player(ObjectType.PLAYER);
			playerId = player.getId();

			this.connectedPlayers.addConnection(player, packet.getIpAddress(), packet.getPort());
			this.gameState.getAllPlayers().add(player);
			this.gameState.getObjects().add(player);

			System.out.println("Number of players connected: " + gameState.getAllPlayers().size());

			System.out.println("New player connection. P: " + playerId + " from: " + packet.getIpAddress());

		}

		Packet response = new ConnectAckPacket(playerId, status);
		this.dispatcher.send(response, packet.getIpAddress(), packet.getPort());

		if (status == ConnectAckPacket.Status.SUC_CONNECTED) {
            this.dispatcher.broadcastConnectedUser(playerId);
		}
	}

	public void receiveInitialGameStateAck(InitialGameStateAckPacket packet) {
		PlayerConnection playerConn = getPlayerConnection(packet);

		if (playerConn != null) {
			playerConn.setHasInitialState();
		} else {
			// Player connection not found
		}
	}

	public void receiveLocationState(LocationStatePacket packet) {
		PlayerConnection playerConn = getPlayerConnection(packet);

		if (playerConn != null) {
			Player player = playerConn.getPlayer();

			// Just update the location for now
			// TODO - validate if the location
			player.setLocation(packet.getX(), packet.getY());
			Rotate playerAngle = player.getPlayerAngle();
			playerAngle.setAngle(packet.getPlayerAngle());
			/*float playerCurrentHealth = packet.getPlayerHealth();
			player.setHealth(playerCurrentHealth); */
		} else {
			// Player connection not found
		}
	}

	public void receiveActionState(ActionStatePacket packet) {
		//currently set to broadcast if and only if there are currently more than 2 players
		boolean isStarted = this.gameState.isStarted();
		int numberOfPlayers = this.gameState.getAllPlayers().size();

	    PlayerConnection playerConn = getPlayerConnection(packet);
	    playerConn.getPlayer().doAction(packet.getAction());

	    if(isStarted && (numberOfPlayers >= 2) ){
            this.dispatcher.broadcastActionState(playerConn.getId(), packet.getAction());
	    }
	}

	public void receiveElementState(ElementStatePacket packet) {
		boolean isStarted = this.gameState.isStarted();
		int numberOfPlayers = this.gameState.getAllPlayers().size();

		PlayerConnection playerConn = getPlayerConnection(packet);
		playerConn.getPlayer().setCurrentElement(packet.getPlayerElementState());

		if(isStarted && (numberOfPlayers >= 2)) {
            this.dispatcher.broadcastElementState(playerConn.getId(), packet.getPlayerElementState());
	    }
	}

	/**
	 * Get the {@link PlayerConnection} for the sender of the Packet.
	 *
	 * @param packet The received packet.
	 * @return The {@link PlayerConnection} the packet was received from.
	 */
	private PlayerConnection getPlayerConnection(Packet packet) {
		return this.connectedPlayers.getPlayerConnection(packet.getIpAddress(), packet.getPort());
    }

}
