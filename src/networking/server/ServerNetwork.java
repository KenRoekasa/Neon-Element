package networking.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
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
    private ArrayList<PlayerConnection> connections;
    protected ServerNetworkDispatcher dispatcher;

    public ServerNetwork(ServerGameState gameState) {
        try {
            socket = new DatagramSocket(Constants.SERVER_LISTENING_PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        this.connections = new ArrayList<>();
        this.running = true;
        this.dispatcher = new ServerNetworkDispatcher(this.socket, gameState);
    }

    public ServerNetworkDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public void close() {
        this.running = false;
        this.dispatcher.close();
    }

    public void run() {
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

        switch(packet.getType()) {
            case HELLO:
                this.dispatcher.receiveHello((HelloPacket) packet);
                break;
            default:
                // TODO: log invalid packet
        }
    }
}
