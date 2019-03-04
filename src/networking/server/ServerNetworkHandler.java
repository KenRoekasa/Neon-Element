package networking.server;

import engine.entities.Player;
import engine.enums.ObjectType;
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
		// int players = this.gameState.getAllPlayers().size();
		// int maxPlayers = this.gameState.getMaxPlayers();
		//
		// Packet response = new HelloAckPacket(players, maxPlayers,
		// packet.getIpAddress(), packet.getPort());
		// System.out.println("respond");
		// this.send(response);
	}

	public void receiveConnect(ConnectPacket packet) {
		boolean isStarted = this.gameState.isStarted();
		boolean hasSpace = this.gameState.getAllPlayers().size() < this.gameState.getMaxPlayers();
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

	public void receiveLocationState(LocationStatePacket packet) {
		PlayerConnection playerConn = getPlayerConnection(packet);

		if (playerConn != null) {
			Player player = playerConn.getPlayer();

			// Just update the location for now
			// TODO - validate if the location
			player.setLocation(packet.getX(), packet.getY());
		} else {
			// Player connection not found
		}
	}

	public void receiveActionState(ActionStatePacket packet) {
	    PlayerConnection playerConn = getPlayerConnection(packet);
	    playerConn.getPlayer().doAction(packet.getAction());
	}

	public void receiveElementState(ElementStatePacket packet) {
		PlayerConnection playerConn = getPlayerConnection(packet);
		playerConn.getPlayer().setCurrentElement(packet.getPlayerElementState());
	}

	private PlayerConnection getPlayerConnection(Packet packet) {
		return this.connectedPlayers.getPlayerConnection(packet.getIpAddress(), packet.getPort());
    }

}
