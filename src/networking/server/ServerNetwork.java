package networking.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.UUID;

import networking.packets.*;
import server.ServerGameState;
import networking.Constants;

public class ServerNetwork extends Thread {
	//changed it to protected
	//private UUID severUID = UUID.randomUUID();

    protected boolean running;

    protected DatagramSocket socket;

    // protected MulticastSocket multicastSocket;

    protected ServerNetworkDispatcher dispatcher;

    public ServerNetwork(ServerGameState gameState) {
        InetAddress groupAddress = null;
        try {
            socket = new DatagramSocket(Constants.SERVER_LISTENING_PORT);

            // Multicast socket
            /*
            groupAddress = InetAddress.getByName(Constants.GROUP_SERVER_ADDRESS);
            multicastSocket = new MulticastSocket(Constants.BROADCASTING_PORT); // TODO does this need to be a different port?
            */
            } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }/* catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/ catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.dispatcher = new ServerNetworkDispatcher(this.socket, /*this.multicastSocket, groupAddress,*/ gameState);
    }

    public ServerNetworkDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public void close() {
        this.running = false;
        this.dispatcher.close();
    }

    public void run() {
        this.running = true;
        while (this.running) {
            byte[] data = new byte[Packet.PACKET_BYTES_LENGTH];

            DatagramPacket packet = new DatagramPacket(data, data.length);

            try {
                this.socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.parse(packet);
        }
    }

    protected void parse(DatagramPacket datagram) {
        Packet packet = Packet.createFromBytes(datagram.getData(), datagram.getAddress(), datagram.getPort());

        if (packet == null) {
            System.out.println("Invalid packet recieved");
            return;
        }

        switch(packet.getType()) {
            case HELLO:
            		System.out.println("recieved ");
                this.dispatcher.receiveHello((HelloPacket) packet);
                break;
            case CONNECT:
                this.dispatcher.receiveConnect((ConnectPacket) packet);
                break;
            default:
                // TODO: log invalid packet
        }
    }
}
