package networking.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import client.ClientGameState;
import client.GameStateGenerator;
import engine.ScoreBoard;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.gameTypes.FirstToXKillsGame;
import javafx.scene.shape.Rectangle;
import server.GameServer;
import server.ServerGameState;

public class ManualTestServer {
    public static void main(String[] args) throws IOException {
        // todo when connected initialise scoreboard
        ServerGameState gameState = new ServerGameState(new Rectangle(2000, 2000), new LinkedBlockingQueue<Player>(),new ArrayList<PhysicsObject>(), new ScoreBoard(), new FirstToXKillsGame(3));
        GameServer server = new GameServer(gameState);
        server.start();

        System.in.read();
    }
}
