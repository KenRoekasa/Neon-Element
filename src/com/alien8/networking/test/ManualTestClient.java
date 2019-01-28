package com.alien8.networking.test;

import com.alien8.networking.client.ClientNetworkDispatcher;
import com.alien8.networking.client.ClientNetwork;

public class ManualTestClient {
    public static void main(String[] args) {
        ClientNetwork net = new ClientNetwork();
        net.start();

        ClientNetworkDispatcher dispatcher = net.getDispatcher();
        dispatcher.sendHello();
        
        net.close();
    }
}
