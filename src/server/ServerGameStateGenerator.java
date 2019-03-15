package server;

import engine.model.*;
import engine.ai.controller.AiControllersManager;
import engine.entities.PhysicsObject;
import engine.model.gametypes.*;
import engine.model.generator.MapGenerator;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class ServerGameStateGenerator {

    public static ServerGameState createEmptyState() {
        ArrayList<PhysicsObject> objects = new ArrayList<>();
        ScoreBoard scoreboard = new ScoreBoard();

        GameType gameType = new FirstToXKillsGame(3);

        Rectangle map = new Rectangle(2000, 2000);
        Map map1 = MapGenerator.createEmptyMap();

        AiControllersManager aiManager = new AiControllersManager(objects, map, null, scoreboard, gameType);

        ServerGameState gameState = new ServerGameState(map1, objects, scoreboard, gameType, aiManager);

        return gameState;
    }

}
