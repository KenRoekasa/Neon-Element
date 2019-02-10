package networking.test;

import networking.client.ClientNetwork;
import networking.client.ClientNetworkDispatcher;

public class ManualTestClient {
    public static void main(String[] args) {
    		//GameState gs = new GameState()
        ClientNetwork net = new ClientNetwork();
        net.start();

        ClientNetworkDispatcher dispatcher = net.getDispatcher();
        dispatcher.sendHello();
        
        while(true) {}
    }
}
