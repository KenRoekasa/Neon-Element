package server;

import java.io.IOException;

import engine.model.GameType;
import engine.model.generator.ServerGameStateGenerator;

public class ServerLauncher {
    public static void main(String[] args) throws IOException {
        int numPlayers = Integer.parseInt(args[0]);
        int numAis = Integer.parseInt(args[1]);
        String aiTypesString = args[2];
        String[] aiTypes = aiTypesString.split(",");
        GameType.Type gameType = GameType.Type.valueOf(args[3]);

        // todo when connected initialise scoreboard
        ServerGameState gameState = ServerGameStateGenerator.createEmptyState(numPlayers, numAis, aiTypes, gameType);
        GameServer server = new GameServer(gameState);
        server.start();

        System.in.read();
    }
}
