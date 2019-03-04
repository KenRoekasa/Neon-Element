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
        } else if (!(packet instanceof Packet.PacketToClient)) {
            System.out.println(packet.getPacketType() + " received by client which should not be sent to it.");
            return;
        }

        if (!packet.getPacketType().equals(Packet.PacketType.LOCATION_STATE_BCAST)) {
            if (packet.getIpAddress() != null) {
                System.out.println("Got " + packet.getPacketType() + " from " + packet.getIpAddress() + ":" + packet.getPort());
            } else {
                System.out.println("Got " + packet.getPacketType());
            }
        }

        ((Packet.PacketToClient) packet).handle(this.dispatcher);
    }

}
