package networking.client;

import java.net.DatagramSocket;
import java.net.InetAddress;

import networking.packets.*;
import networking.Constants;
import networking.NetworkDispatcher;

public class ClientNetworkDispatcher extends NetworkDispatcher {
	
	protected String serverAddress; 
    protected ClientNetworkDispatcher(DatagramSocket socket) {
        super(socket);
    }
    
    protected ClientNetworkDispatcher(DatagramSocket socket, String serverAddress) {
        super(socket);
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
    
    protected String getServerAddress() {
    	return serverAddress;
    }

}
