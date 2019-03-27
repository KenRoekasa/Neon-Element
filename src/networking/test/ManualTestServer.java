package networking.test;

import java.io.IOException;
import server.GameServer;
import server.ServerGameState;
import server.ServerGameStateGenerator;

public class ManualTestServer {
    public static void main(String[] args) throws IOException {
        int numPlayers = Integer.parseInt(args[0]);


        // todo when connected initialise scoreboard
        ServerGameState gameState = ServerGameStateGenerator.createEmptyState(numPlayers);
        GameServer server = new GameServer(gameState);
        server.start();

        System.in.read();
    }
}
