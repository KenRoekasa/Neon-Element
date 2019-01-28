package Networking.Test;

import Networking.Server.ServerNetwork;

public class ManualTestServer {
    public static void main(String[] args) {
        ServerNetwork net = new ServerNetwork();
        net.start();

        while(true) {}
    }
}
