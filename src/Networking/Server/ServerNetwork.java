package Networking.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

import Networking.Constants;
import Networking.Packets.*;

public class ServerNetwork extends Thread {
    private boolean running;
    private DatagramSocket socket;
    private ArrayList<PlayerConnection> connections;
    private ServerNetworkDispatcher dispatcher;

    public ServerNetwork() {
        try {
            socket = new DatagramSocket(Constants.SERVER_LISTENING_PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        this.connections = new ArrayList<>();
        this.running = true;
        this.dispatcher = new ServerNetworkDispatcher(this.socket);
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

    private void parse(DatagramPacket datagram) {
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
