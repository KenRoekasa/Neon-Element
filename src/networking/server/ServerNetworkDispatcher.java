package networking.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import engine.entities.PowerUp;
import engine.enums.Action;
import engine.enums.Elements;
import networking.packets.*;
import server.ServerGameState;
import networking.AbstractNetworkDispatcher;

public class ServerNetworkDispatcher extends AbstractNetworkDispatcher {

	private ServerGameState gameState;
	private ConnectedPlayers connectedPlayers;

	protected ServerNetworkDispatcher(ServerGameState gameState, ConnectedPlayers connectedPlayers, DatagramSocket socket) {
		super(socket);
		this.gameState = gameState;
		this.connectedPlayers = connectedPlayers;
	}

	public void broadcastGameState() {
		Packet packet = new InitialGameStateBroadcast(this.gameState.getMap(), connectedPlayers.getIds(), connectedPlayers.getLocations());
        this.broadcast(packet);
	}

	public void broadcastGameStarted() {
		Packet packet = new GameStartBroadcast(true, this.gameState.getAllPlayers().size());
		this.broadcast(packet);
	}

	protected void broadCastNewConnectedUser() {
		// Packet response = new BroadCastConnectedUserPacket(new Buffer());
		// this.send(response);
	}

    public void broadcastNewPowerUp(PowerUp powerUp) {
        double x = powerUp.getLocation().getX();
        double y = powerUp.getLocation().getY();
        Packet packet = new PowerUpBroadcast(powerUp.getId(), x, y, powerUp.getType());
        this.broadcast(packet);
    }

    public void broadcastLocationState(int playerId, double x, double y, double playerAngle) {
        Packet packet = new LocationStateBroadcast(playerId, x, y, playerAngle);
        this.broadcast(packet);
    }

    public void broadcastElementState(int playerId, Elements element) {
    	Packet packet = new ElementStateBroadcast(playerId, element);
    	this.broadcast(packet);
	}

	public void broadcastActionState(int playerId, Action action) {
		Packet packet = new ActionStateBroadcast(playerId, action);
		this.broadcast(packet);
	}

	public void broadcastConnectedUser(int playerId) {
		Packet packet = new ConnectBroadcast(playerId);
		this.broadcast(packet);
	}

	public void broadCastDisconnectedUser(DisconnectAckPacket packet) {
		// TODO Auto-generated method stub
		Packet response = new DisconnectAckPacket(true);
		this.send(response, packet.getIpAddress(), packet.getPort());
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

	/**
	 * Broadcast the Packet to all connected clients.
	 *
	 * @param packet
	 */
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
            System.out.println("Attempted to send a received packet.");
        }
    }

}
