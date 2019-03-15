package networking.test;

import java.io.IOException;
import server.GameServer;
import server.ServerGameState;
import server.ServerGameStateGenerator;

public class ManualTestServer {
    public static void main(String[] args) throws IOException {
        // todo when connected initialise scoreboard
        ServerGameState gameState = ServerGameStateGenerator.createEmptyState();
        GameServer server = new GameServer(gameState);
        server.start();

        System.in.read();
    }
}
