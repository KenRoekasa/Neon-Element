package com.alien8.networking.client;

import java.net.DatagramSocket;

import com.alien8.networking.NetworkDispatcher;
import com.alien8.networking.packets.*;

public class ClientNetworkDispatcher extends NetworkDispatcher {

    protected ClientNetworkDispatcher(DatagramSocket socket) {
        super(socket);
    }
    
    public void sendHello() {
        // TODO create and send hello packet
    }

    protected void receiveHelloAck(HelloAckPacket packet) {
        int players = packet.getPlayers();
        int maxPlayers = packet.getMaxPlayers();
        // TODO - integrate and send these values somewhere
    }

}
