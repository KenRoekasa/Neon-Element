package networking.server;

import java.net.DatagramSocket;

import networking.packets.*;
import server.ServerGameState;
import networking.NetworkDispatcher;

public class ServerNetworkDispatcher extends NetworkDispatcher {
    
    private ServerGameState gameState;

	protected ServerNetworkDispatcher(DatagramSocket socket, ServerGameState gameState) {
		super(socket);
		this.gameState = gameState;
	}

	protected void receiveHello(HelloPacket packet) {
		// TODO - integrate and get these values from somewhere
		int players = 0;
		int maxPlayers = 0;
		Packet response = new HelloAckPacket(players, maxPlayers, packet.getIpAddress(), packet.getPort());
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
