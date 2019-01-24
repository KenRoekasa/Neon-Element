package com.alien8.networking.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

import com.alien8.networking.Constants;
import com.alien8.networking.packets.*;

public class ServerNetwork extends Thread {
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

        this.dispatcher = new ServerNetworkDispatcher(this.socket);
    }

    public void run() {
        while (true) {
            byte[] data = new byte[1];
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
