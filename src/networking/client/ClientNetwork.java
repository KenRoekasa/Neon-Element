package networking.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.SocketException;

import client.ClientGameState;
import networking.packets.*;

public class ClientNetwork extends Thread {
    protected String name;
    
    protected DatagramSocket socket;
    protected MulticastSocket multicastSocket;
    
    private ClientNetworkConnection conn;
    private ClientNetworkMulticastConnection multiConn;
    private ClientNetworkDispatcher dispatcher;

    public ClientNetwork(ClientGameState gameState) {
        this.dispatcher = new ClientNetworkDispatcher(this.socket, gameState);

        this.conn = new ClientNetworkConnection(this);
        this.socket = conn.getSocket();

        this.multiConn = new ClientNetworkMulticastConnection(this);
        this.multicastSocket = multiConn.getSocket();
    }

    //This client cons takes in the serverAddresws and playerName in as an arg from the command line
    public ClientNetwork(String name, String serverAddress) {
        try {
            this.socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        this.dispatcher = new ClientNetworkDispatcher(this.socket, serverAddress);
        this.name = name;
    }
    
    public ClientNetworkDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public void close() {
        this.conn.close();
        this.multiConn.close();
        this.dispatcher.close();
    }

    protected void parse(DatagramPacket datagram) {
        Packet packet = Packet.createFromBytes(datagram.getData(), datagram.getAddress(), datagram.getPort());
        
        switch(packet.getType()) {
            case HELLO_ACK:
                this.dispatcher.receiveHelloAck((HelloAckPacket) packet);
            case GAME_START_BCAST:
            		
                break;
            case LOCATION_STATE_BCAST:
            		break;
            case DISCONNECT_BCAST:
            		break;
            case ELEMENT_STATE_BCAST:
            		break;
            case GAME_OVER_BCAST:
            		break;
            case POWERUP_PICKUP_BCAST:
            		break;
            case POWERUP_STATE_BCAST:
            		break;
            case READY_STATE_BCAST:
            		break;
            case CONNECT_BCAST:
            		break;
            case CAST_SPELL_BCAST:
            		break;
     
            default:
                System.out.println("Invalid packet recieved");
        }
    }

}
