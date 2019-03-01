package networking.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import client.ClientGameState;
import networking.packets.*;

public class ClientNetwork {
    protected String name;
    
    protected DatagramSocket socket;
    // protected MulticastSocket multicastSocket;
    
    private ClientNetworkConnection conn;
    // private ClientNetworkMulticastConnection multiConn;
    private ClientNetworkDispatcher dispatcher;

    public ClientNetwork(ClientGameState gameState, InetAddress serverAddr) {
        this.conn = new ClientNetworkConnection(this);
        this.socket = conn.getSocket();

        /*
        this.multiConn = new ClientNetworkMulticastConnection(this);
        this.multicastSocket = multiConn.getSocket();
        InetAddress groupAddress = multiConn.getGroupAddress();
        */

        this.dispatcher = new ClientNetworkDispatcher(this.socket, serverAddr, /*this.multicastSocket, groupAddress,*/ gameState);

        this.conn.start();
        // this.multiConn.start();
    }
    
    public ClientNetworkDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public void close() {
        this.conn.close();
        // this.multiConn.close();
        this.dispatcher.close();
    }

    protected void parse(DatagramPacket datagram) {
        Packet packet = Packet.createFromBytes(datagram.getData(), datagram.getAddress(), datagram.getPort());

        if (packet == null) {
            System.out.println("Invalid packet recieved");
            return;
        }

        if ((!packet.getType().equals(Packet.PacketType.LOCATION_STATE_BCAST)) && (packet.getIpAddress() != null)) {
            System.out.println("recieved a packet pf type: "+packet.getType()+" <== " + packet.getIpAddress() + " and port: " + packet.getPort()  );
            
        }else {
            System.out.println("recieved a broacdcast packet of type: "+packet.getType());


        }
        switch(packet.getType()) {
            case HELLO_ACK:
                this.dispatcher.receiveHelloAck((HelloAckPacket) packet);
                break;
            case CONNECT_ACK:
                this.dispatcher.receiveConnectAck((ConnectAckPacket) packet);
                break;
            case GAME_START_BCAST:
                this.dispatcher.receiveGameStart((BroadCastGameStartPacket) packet);
                break;
            case LOCATION_STATE_BCAST:
                this.dispatcher.receiveLocationStateBroadcast((BroadCastLocationStatePacket) packet);
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
                this.dispatcher.receivePowerUpBroadcast((BroadCastPowerUpPacket) packet);
                break;
            case READY_STATE_BCAST:
            		break;
            case CONNECT_BCAST:
            		break;
            case CAST_SPELL_BCAST:
            		break;
            default:
                System.out.println("Unhandled packet " + packet.getType());
                break;
        }
    }

}
