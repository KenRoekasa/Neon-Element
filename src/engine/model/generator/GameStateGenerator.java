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
        scoreboard.initialise(gameState.getAllPlayers());

        return gameState;
    }

    public static ClientGameState createDemoGamestate() {
//    	System.out.println("generating game state");
//        //initialise map location
//        Rectangle map = new Rectangle(2000, 2000);
//
//        // create player
//        Player player = new Player(ObjectType.PLAYER);
//        player.setId(100);
//        Point2D playerStartLocation = new Point2D(500, 500);
//        player.setLocation(playerStartLocation);
//
//        // create object list
//        ArrayList<PhysicsObject> objects = new ArrayList<>();
//
//        // create power up
//        PowerUp pu = new PowerUp();
//        objects.add(pu);
//
//        // create enemies lists'
//        ArrayList<Player> enemies = new ArrayList<>();
//        ArrayList<AiController> aiConList = new ArrayList<>();
//        GameType gameType = new FirstToXKillsGame(10);
//        ScoreBoard scoreboard = new ScoreBoard();
//        // create an enemy and its ai controller
//        AiController aiCon = new AiController( new Player(ObjectType.ENEMY), objects, map ,player, AiType.EASY, scoreboard, gameType);
//        aiConList.add(aiCon);
//        enemies.add(aiCon.getAiPlayer() );
//        enemies.get(0).setLocation(new Point2D(140, 100));
//
//        // Add the enemies to the objects list
//        objects.addAll(enemies);
//        objects.add(player);
//        // generate a game state
//        LinkedBlockingQueue deadPlayers = new LinkedBlockingQueue();
//
//        ClientGameState gameState = new ClientGameState(player, map, objects,deadPlayers, scoreboard, gameType,new AiControllersManager());
//
//        //This will be initialised on start of the game
//        scoreboard.initialise(gameState.getAllPlayers());
//        // start the engine.ai
//        //startAi(aiConList);
//
        return null;
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
                gameType = new Regicide(player.getId(), 5000);
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
            ((Regicide)gameType).setKingId(enemies.get(0).getId());
        }

        ClientGameState gameState = new ClientGameState(player, map, objects, scoreboard, gameType,aiManager,mode);

        scoreboard.initialise(gameState.getAllPlayers());

//        aiManager.startAllAi();



        return gameState;
    }

}
