package server;

import engine.ai.enums.AiType;
import engine.entities.Player;
import engine.model.*;
import engine.ai.controller.AiControllersManager;
import engine.entities.PhysicsObject;
import engine.model.gametypes.*;
import engine.model.generator.MapGenerator;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class ServerGameStateGenerator {

    public static ServerGameState createEmptyState(int numPlayers, int num_enm, String[] aiTypes, GameType.Type mode) {
        ArrayList<PhysicsObject> objects = new ArrayList<>();
        ScoreBoard scoreboard = new ScoreBoard();

        GameType gameType = null;

        switch (mode){
            case FirstToXKills:
                gameType = new FirstToXKillsGame(10);
                break;
            case Timed:
                gameType =  new TimedGame(90000);
                break;
            case Hill:
                gameType = new HillGame(new Circle(2000, 0, 500),100000);
                break;
            case Regicide:
                gameType = new Regicide(1, 5000);
                break;
            default:
                gameType = new FirstToXKillsGame(10);
        }

        Map map = MapGenerator.createEmptyMap();

        ArrayList<Player> enemies = new ArrayList<>();

        AiControllersManager aiManager = new AiControllersManager(objects, map.getGround(), scoreboard, gameType);

        for (int i = 0; i < num_enm; i++) {
            enemies.add( aiManager.addAi(AiType.getType(aiTypes[i])) );
        }

        for (int i = 0; i < num_enm; i++) {
            enemies.get(i).setLocation(map.getRespawnPoints().get(i+1));
            enemies.get(i).setId(i);
        }

        //Create the first map

        //Add the enemies to the objects list
        objects.addAll(enemies);

        if(gameType.getType() == GameType.Type.Regicide) {
            ((Regicide)gameType).setKingId(enemies.get(0).getId());
        }

        ServerGameState gameState = new ServerGameState(map, objects, scoreboard, gameType, aiManager, numPlayers);

        return gameState;
    }

}
