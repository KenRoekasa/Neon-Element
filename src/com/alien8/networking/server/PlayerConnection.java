package com.alien8.networking.server;

import java.net.InetAddress;

public class PlayerConnection {
    private InetAddress ipAddress;
    private int port;

    public PlayerConnection(InetAddress ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

}
