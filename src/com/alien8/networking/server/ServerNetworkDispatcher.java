package com.alien8.networking.server;

import java.net.DatagramSocket;

import com.alien8.networking.NetworkDispatcher;
import com.alien8.networking.packets.*;

public class ServerNetworkDispatcher extends NetworkDispatcher {

    protected ServerNetworkDispatcher(DatagramSocket socket) {
        super(socket);
    }

    protected void receiveHello(HelloPacket packet) {
        // TODO - integrate and get these values from somewhere
        int players = 0;
        int maxPlayers = 0;
        Packet response = new HelloAckPacket(players, maxPlayers, packet.getIpAddress(), packet.getPort());
        this.send(response);
    }

}
