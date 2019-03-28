package engine.model.generator;

import client.ClientGameState;
import engine.ai.controller.AiControllersManager;
import engine.ai.enums.AiType;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.model.GameType;
import engine.model.gametypes.*;
import engine.model.Map;
import engine.model.ScoreBoard;
import engine.model.enums.ObjectType;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class GameStateGenerator {

    public static ClientGameState createEmptyState() {
        ArrayList<PhysicsObject> objects = new ArrayList<>();
        ScoreBoard scoreboard = new ScoreBoard();

        Map map = MapGenerator.createEmptyMap();
        GameType gameType = new FirstToXKillsGame(3);

        AiControllersManager aiManager = new AiControllersManager(objects, map.getGround(), null, gameType);

        ClientGameState gameState = new ClientGameState(null, map, objects, scoreboard, gameType, aiManager, GameType.Type.FirstToXKills);

        return gameState;
    }

    //receive the number of enemy from controller to initialise engine.ai enm

    /**
     * Generates a game state for testing
     *
     * @param num_enm the number ais
     * @param aiTypes A list of ais diffculty
     * @return a gamestate for testing
     */
    public static ClientGameState createDemoGamestateSample(int num_enm, ArrayList<String> aiTypes,GameType.Type mode) {
        Map map = MapGenerator.createEmptyMap();

        // create player
        Player player = new Player(ObjectType.PLAYER);
        player.setId(4);


        //add the 1 power up to the objects list

        ArrayList<PhysicsObject> objects = new ArrayList<>();
        ScoreBoard scoreboard = new ScoreBoard();
        GameType gameType = null;

        // First to 10 kills
        switch (mode){

            case FirstToXKills:
                gameType = new FirstToXKillsGame(10);
                break;
            case Timed:
                gameType =  new TimedGame(90000);

                break;
            case Hill:
                gameType = new HillGame(new Circle(1000, 1000, 500),100000);
                break;
            case Regicide:
                gameType = new Regicide(player, 5000);
                break;
                default:
                gameType = new FirstToXKillsGame(10);


        }


        // initialise enemies
        ArrayList<Player> enemies = new ArrayList<>();

        AiControllersManager aiManager = new AiControllersManager(objects, map.getGround(), scoreboard, gameType);

        // Add the enemies to the objects list


        for (int i = 0; i < num_enm; i++) {
            enemies.add( aiManager.addAi(AiType.getType(aiTypes.get(i))) );
        }


        player.setLocation(map.getRespawnPoints().get(0));

        for (int i = 0; i < num_enm; i++) {
            enemies.get(i).setLocation(map.getRespawnPoints().get(i+1));
            enemies.get(i).setId(i);
        }

        //Create the first map

        //Add the enemies to the objects list
        objects.addAll(enemies);
        objects.add(player);
        objects.addAll(map.getWalls());

        if(gameType.getType() == GameType.Type.Regicide) {
            ((Regicide)gameType).setKing(enemies.get(0));
        }

        ClientGameState gameState = new ClientGameState(player, map, objects, scoreboard, gameType,aiManager,mode);

        scoreboard.initialise(gameState.getAllPlayers());

//        aiManager.startAllAi();



        return gameState;
    }

}
