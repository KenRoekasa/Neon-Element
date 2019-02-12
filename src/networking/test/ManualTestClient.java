package networking.test;

import java.io.IOException;

import client.ClientGameState;
import client.GameStateGenerator;
import networking.client.ClientNetwork;
import networking.client.ClientNetworkDispatcher;

public class ManualTestClient {
    public static void main(String[] args) throws IOException {
    		//GameState gs = new GameState()
        ClientGameState gameState = GameStateGenerator.createDemoGamestate();
        ClientNetwork net = new ClientNetwork(gameState);

        ClientNetworkDispatcher dispatcher = net.getDispatcher();
        dispatcher.sendHello();

        System.in.read();
    }
}
