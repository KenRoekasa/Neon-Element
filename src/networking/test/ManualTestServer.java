package networking.test;

import java.util.ArrayList;

import client.ClientGameState;
import client.GameStateGenerator;
import entities.Enemy;
import entities.Player;
import networking.server.ServerNetwork;
import server.ServerGameState;

public class ManualTestServer {
    public static void main(String[] args) {
        ClientGameState clientGameState = GameStateGenerator.createDemoGamestate();
        ServerGameState gameState = new ServerGameState(new ArrayList<Player>(), new ArrayList<Enemy>(), clientGameState.getMap(), clientGameState.getObjects());
        ServerNetwork net = new ServerNetwork(gameState);
        net.start();

        while(true) {}
    }
}
