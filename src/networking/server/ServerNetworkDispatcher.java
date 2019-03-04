package networking.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

import engine.entities.Player;
import engine.entities.PowerUp;
import engine.enums.Action;
import engine.enums.Elements;
import engine.enums.ObjectType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import networking.packets.*;
import server.ServerGameState;
import networking.NetworkDispatcher;


public class ServerNetworkDispatcher extends NetworkDispatcher {

	private ServerGameState gameState;
	private ConnectedPlayers connectedPlayers;

	protected ServerNetworkDispatcher(ServerGameState gameState, ConnectedPlayers connectedPlayers, DatagramSocket socket) {
		super(socket/* , multicastSocket, groupAddress */);
		this.gameState = gameState;
		this.connectedPlayers = connectedPlayers;
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
		this.send(response, packet.getIpAddress(), packet.getPort());

		if (status == ConnectAckPacket.Status.SUC_CONNECTED) {
			Packet connect = new BroadCastConnectedUserPacket(playerId);
			this.broadcast(connect);
		}
	}

	public void broadcastGameState() {
		Packet packet = new BroadCastinitialGameStatePacket(gameState.getGameType(), connectedPlayers.getIds(), connectedPlayers.getLocations(), this.gameState.getMap());
        this.broadcast(packet);
	}

	public void broadcastGameStarted() {
		Packet packet = new BroadCastGameStartPacket(true, this.gameState.getAllPlayers().size());
		this.broadcast(packet);
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

	protected void broadCastNewConnectedUser() {
		// Packet response = new BroadCastConnectedUserPacket(new Buffer());
		// this.send(response);
	}




    public void broadcastNewPowerUp(PowerUp powerUp) {
        double x = powerUp.getLocation().getX();
        double y = powerUp.getLocation().getY();
        Packet packet = new BroadCastPowerUpPacket(powerUp.getId(), x, y, powerUp.getType());
        this.broadcast(packet);
    }

    public void broadcastLocationState(int playerId, double x, double y) {
        Packet packet = new BroadCastLocationStatePacket(playerId, x, y);
        this.broadcast(packet);
    }

    public void broadcastElementState(int playerId, Elements element) {
    	Packet packet = new BroadCastElementStatePacket(playerId, element);
    	this.broadcast(packet);
	}

	public void broadcastAttackState(int playerId, Action action) {
		Packet packet = new BroadcastActionPacket(playerId, action);
		this.broadcast(packet);
	}

	public void receiveActionState(ActionStatePacket packet) {
	    PlayerConnection playerConn = getPlayerConnection(packet);
	    playerConn.getPlayer().doAction(packet.getAction());
	}


	public void receiveElementState(ElementStatePacket packet) {

		PlayerConnection playerConn = getPlayerConnection(packet);
		playerConn.getPlayer().setCurrentElement(packet.getPlayerElementState());
	}


	public void broadCastDisconnectedUser(DisconnectAckPacket packet) {
		// TODO Auto-generated method stub
		Packet response = new DisconnectAckPacket(true);
		this.send(response, packet.getIpAddress(), packet.getPort());
	}

	private PlayerConnection getPlayerConnection(Packet packet) {
		return this.connectedPlayers.getPlayerConnection(packet.getIpAddress(), packet.getPort());
	}

    private void broadcast(Packet packet) {
        if (packet.getDirection() == Packet.PacketDirection.OUTGOING) {
            byte[] data = packet.getRawBytes();

            for (PlayerConnection conn : this.connectedPlayers.getConnections()) {
                DatagramPacket datagram = new DatagramPacket(data, data.length, conn.getIpAddress(), conn.getPort());

                if (!packet.getPacketType().equals(Packet.PacketType.LOCATION_STATE_BCAST)) {
                    System.out.println("Sent " + packet.getPacketType() + " to " + conn.getIpAddress() + ":" +conn.getPort() + " (" + conn.getId() + ")");
                }

                try {
                    this.socket.send(datagram);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Attempted to send a recived packet.");
        }
    }

	private void send(Packet packet, PlayerConnection conn) {
		super.send(packet, conn.getIpAddress(), conn.getPort());
	}

}
