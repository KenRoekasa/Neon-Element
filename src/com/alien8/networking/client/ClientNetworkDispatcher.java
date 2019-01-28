package com.alien8.networking.client;

import java.net.DatagramSocket;
import java.net.InetAddress;

import com.alien8.networking.Constants;
import com.alien8.networking.NetworkDispatcher;
import com.alien8.networking.packets.*;

public class ClientNetworkDispatcher extends NetworkDispatcher {

    protected ClientNetworkDispatcher(DatagramSocket socket) {
        super(socket);
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
        System.out.println("Got players" + players + " max:" + maxPlayers);
    }

}
