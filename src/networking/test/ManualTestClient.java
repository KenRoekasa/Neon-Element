package networking.test;

import client.ClientGameState;
import client.GameStateGenerator;
import networking.client.ClientNetwork;
import networking.client.ClientNetworkDispatcher;

public class ManualTestClient {
    public static void main(String[] args) {
    		//GameState gs = new GameState()
        ClientGameState gameState = GameStateGenerator.createDemoGamestate();
        ClientNetwork net = new ClientNetwork(gameState);
        net.start();

        ClientNetworkDispatcher dispatcher = net.getDispatcher();
        dispatcher.sendHello();
        
        while(true) {}
    }
}
