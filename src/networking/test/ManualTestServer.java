package networking.test;

import java.io.IOException;
import java.util.ArrayList;

import client.ClientGameState;
import client.GameStateGenerator;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import server.GameServer;
import server.ServerGameState;

public class ManualTestServer {
    public static void main(String[] args) throws IOException {
        ClientGameState clientGameState = GameStateGenerator.createDemoGamestate();
        //TODO: not sure what this does change the parameters below accordingly - Kenny"
        ServerGameState gameState = new ServerGameState(clientGameState.getMap(), clientGameState.getDeadPlayers(),clientGameState.getObjects(), clientGameState.getScoreBoard(),clientGameState.getGameType());
        GameServer server = new GameServer(gameState);
        server.start();

        System.in.read();
    }
}
