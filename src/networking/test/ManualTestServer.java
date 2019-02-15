package networking.test;

import java.io.IOException;
import java.util.ArrayList;

import client.ClientGameState;
import client.GameStateGenerator;
import entities.Player;
import server.GameServer;
import server.ServerGameState;

public class ManualTestServer {
    public static void main(String[] args) throws IOException {
        ClientGameState clientGameState = GameStateGenerator.createDemoGamestate();
        ServerGameState gameState = new ServerGameState(new ArrayList<Player>(), new ArrayList<Player>(), clientGameState.getMap(), clientGameState.getObjects());
        GameServer server = new GameServer(gameState);
        server.start();

        System.in.read();
    }
}
