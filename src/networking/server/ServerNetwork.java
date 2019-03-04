package networking.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

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
            } /* catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/ catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ConnectedPlayers connectedPlayers = new ConnectedPlayers();

        this.dispatcher = new ServerNetworkDispatcher(gameState, connectedPlayers, this.socket);
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
        } else if (!(packet instanceof Packet.PacketToServer)) {
            System.out.println(packet.getPacketType() + " received by server which should not be sent to it.");
            return;
        }

        if (!packet.getPacketType().equals(Packet.PacketType.LOCATION_STATE)) {
            System.out.println("Got " + packet.getPacketType() + " from " + packet.getIpAddress() + ":" + packet.getPort());
        }

        ((Packet.PacketToServer) packet).handle(this.dispatcher);
    }
}
