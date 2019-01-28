package Networking.Test;

import Networking.Client.ClientNetwork;
import Networking.Client.ClientNetworkDispatcher;

public class ManualTestClient {
    public static void main(String[] args) {
        ClientNetwork net = new ClientNetwork();
        net.start();

        ClientNetworkDispatcher dispatcher = net.getDispatcher();
        dispatcher.sendHello();
        
        net.close();
    }
}
