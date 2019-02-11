package networking.client;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import client.ClientGameState;
import networking.packets.*;
import networking.Constants;
import networking.NetworkDispatcher;

public class ClientNetworkDispatcher extends NetworkDispatcher {

    private ClientGameState gameState;
    
	protected String serverAddress;

    protected ClientNetworkDispatcher(DatagramSocket socket, MulticastSocket multicastSocket, InetAddress groupAddress, ClientGameState gameState) {
        super(socket, multicastSocket, groupAddress);
        this.gameState = gameState;
    }
    
    protected ClientNetworkDispatcher(DatagramSocket socket, MulticastSocket multicastSocket, InetAddress groupAddress, String serverAddress) {
        super(socket, multicastSocket, groupAddress);
        this.serverAddress = serverAddress;
    }

    public void sendHello() {
        try {
            Packet packet = new HelloPacket(InetAddress.getByName(Constants.SERVER_ADDRESS), Constants.SERVER_LISTENING_PORT);
            this.send(packet);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    protected void receiveHelloAck(HelloAckPacket packet) {
        int players = packet.getPlayers();
        int maxPlayers = packet.getMaxPlayers();
        System.out.println("Got players: " + players + " max: " + maxPlayers);
    }

    public void sendLocationState(double x, double y) {
        try {
            Packet packet = new LocationStatePacket(x, y, InetAddress.getByName(Constants.SERVER_ADDRESS), Constants.SERVER_LISTENING_PORT);
            this.send(packet);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    protected String getServerAddress() {
    	return serverAddress;
    }

}
