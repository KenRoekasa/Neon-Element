package networking.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import engine.entities.PowerUp;
import engine.enums.Action;
import engine.enums.Elements;
import networking.packets.*;
import server.ServerGameState;
import networking.NetworkDispatcher;

public class ServerNetworkDispatcher extends NetworkDispatcher {

	private ServerGameState gameState;
	private ConnectedPlayers connectedPlayers;

	protected ServerNetworkDispatcher(ServerGameState gameState, ConnectedPlayers connectedPlayers, DatagramSocket socket) {
		super(socket);
		this.gameState = gameState;
		this.connectedPlayers = connectedPlayers;
	}

	public void broadcastGameState() {
		Packet packet = new BroadCastinitialGameStatePacket(gameState.getGameType(), connectedPlayers.getIds(), connectedPlayers.getLocations(), this.gameState.getMap());
        this.broadcast(packet);
	}

	public void broadcastGameStarted() {
		Packet packet = new BroadCastGameStartPacket(true, this.gameState.getAllPlayers().size());
		this.broadcast(packet);
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

    public void broadcastLocationState(int playerId, double x, double y, double playerAngle) {
        Packet packet = new BroadCastLocationStatePacket(playerId, x, y, playerAngle);
        this.broadcast(packet);
    }

    public void broadcastElementState(int playerId, Elements element) {
    	Packet packet = new BroadCastElementStatePacket(playerId, element);
    	this.broadcast(packet);
	}

	public void broadcastActionState(int playerId, Action action) {
		Packet packet = new BroadcastActionPacket(playerId, action);
		this.broadcast(packet);
	}

	public void broadcastConnectedUser(int playerId) {
		Packet packet = new BroadCastConnectedUserPacket(playerId);
		this.broadcast(packet);
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

}
