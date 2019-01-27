package com.alien8.networking.test;

import com.alien8.networking.server.ServerNetwork;

public class ManualTestServer {
    public static void main(String[] args) {
        ServerNetwork net = new ServerNetwork();
        net.start();

        while(true) {}
    }
}
