package server;

import engine.ScoreBoard;
import engine.entities.PhysicsObject;
import engine.gameTypes.*;

import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerGameStateGenerator {

    public static ServerGameState createEmptyState() {
        ArrayList<PhysicsObject> objects = new ArrayList<>();
        LinkedBlockingQueue deadPlayers = new LinkedBlockingQueue();
        ScoreBoard scoreboard = new ScoreBoard();

        ServerGameState gameState = new ServerGameState(new Rectangle(2000, 2000), deadPlayers, objects, scoreboard, new FirstToXKillsGame(3));

        return gameState;
    }

}
