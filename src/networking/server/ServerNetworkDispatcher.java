package networking.server;

import java.net.DatagramSocket;
import java.util.ArrayList;

import entities.Player;
import networking.packets.*;
import server.ServerGameState;
import networking.NetworkDispatcher;

public class ServerNetworkDispatcher extends NetworkDispatcher {
    
    private ServerGameState gameState;
    
    private int nextPlayerId = 0;
    private ArrayList<PlayerConnection> connections;

	protected ServerNetworkDispatcher(DatagramSocket socket, ServerGameState gameState) {
		super(socket);
		this.gameState = gameState;
        this.connections = new ArrayList<>();
	}

	protected void receiveHello(HelloPacket packet) {
		// TODO - integrate and get these values from somewhere
		int players = this.gameState.getPlayers().size();
		int maxPlayers = 0;
		Packet response = new HelloAckPacket(players, maxPlayers, packet.getIpAddress(), packet.getPort());
		this.send(response);
	}

	protected void receiveConnect(ConnectPacket packet) {
	    // Allow connection if the game has not started yet
	    boolean allowed = !this.gameState.isStarted();
	    
	    if (allowed) {
	        PlayerConnection playerConn = new PlayerConnection(this.nextPlayerId, packet.getIpAddress(), packet.getPort());
	        Player player = new Player(this.nextPlayerId);
	        this.nextPlayerId++;

	        this.connections.add(playerConn);
	        this.gameState.getPlayers().add(player);
	    }

        Packet response = new ConnectAckPacket(allowed, packet.getIpAddress(), packet.getPort());
        this.send(response);
	}

	protected void broadCastNewConnectedUser(ConnectAckPacket packet) {
		Packet response = new ConnectAckPacket(packet.getIpAddress(), packet.getPort(), true);
		this.send(response);
	}

	protected void receiveSpellCast(CastSpellPacket packet) {
		// Packet response = new SpellCas
	}

	public void broadCastDisconnectedUser(DisconnectAckPacket packet) {
		// TODO Auto-generated method stub
		Packet response = new DisconnectAckPacket(packet.getIpAddress(), packet.getPort(), true);
		this.send(response);
	}
	
}
