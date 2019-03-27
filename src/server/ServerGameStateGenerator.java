package server;

import engine.model.*;
import engine.ai.controller.AiControllersManager;
import engine.entities.PhysicsObject;
import engine.model.gametypes.*;
import engine.model.generator.MapGenerator;

import java.util.ArrayList;

public class ServerGameStateGenerator {

    public static ServerGameState createEmptyState(int numPlayers) {
        ArrayList<PhysicsObject> objects = new ArrayList<>();
        ScoreBoard scoreboard = new ScoreBoard();

        GameType gameType = new FirstToXKillsGame(3);

        Map map = MapGenerator.createEmptyMap();

        AiControllersManager aiManager = new AiControllersManager(objects, map.getGround(), null, gameType);

        ServerGameState gameState = new ServerGameState(map, objects, scoreboard, gameType, aiManager, numPlayers);

        return gameState;
    }

}
