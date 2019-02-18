package networking.test;

import java.io.IOException;
import java.util.ArrayList;

import client.ClientGameState;
import client.GameStateGenerator;
import entities.Enemy;
import entities.Player;
import server.GameServer;
import server.ServerGameState;

public class ManualTestServer {
    public static void main(String[] args) throws IOException {
    	System.setProperty("java.net.preferIPv4Stack", "true");

        ClientGameState clientGameState = GameStateGenerator.createDemoGamestate();
        ServerGameState gameState = new ServerGameState(new ArrayList<Player>(), new ArrayList<Enemy>(), clientGameState.getMap(), clientGameState.getObjects());
        GameServer server = new GameServer(gameState);
        server.start();

        System.in.read();
    }
}
