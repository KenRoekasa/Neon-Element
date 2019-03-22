package networking.test;

import java.io.IOException;

import client.ClientGameState;
import engine.model.generator.GameStateGenerator;
import server.GameServer;
import server.ServerGameState;

public class ManualTestServer {
    public static void main(String[] args) throws IOException {
        ClientGameState clientGameState = GameStateGenerator.createDemoGamestate();
        //TODO: not sure what this does change the parameters below accordingly - Kenny"
        ServerGameState gameState = new ServerGameState(clientGameState.getMap(), clientGameState.getObjects(), clientGameState.getScoreBoard(),clientGameState.getGameType(),clientGameState.getAiConMan());
        GameServer server = new GameServer(gameState);
        server.start();

        System.in.read();
    }
}
