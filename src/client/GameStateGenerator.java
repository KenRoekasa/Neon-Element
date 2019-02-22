package client;


import engine.ScoreBoard;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.entities.PowerUp;
import engine.enums.ObjectType;
import engine.gameTypes.FirstToXKillsGame;
import engine.gameTypes.GameType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import engine.ai.AiController;

public class GameStateGenerator {

    public static ClientGameState createDemoGamestate() {
    	System.out.println("generating game state");
        //initialise map location
        Rectangle map = new Rectangle(2000, 2000);

        // create player
        Player player = new Player(ObjectType.PLAYER);
        player.setId(100);
        Point2D playerStartLocation = new Point2D(500, 500);
        player.setLocation(playerStartLocation);
        
        // create object list
        ArrayList<PhysicsObject> objects = new ArrayList<>();
        
        // create power up
        PowerUp pu = new PowerUp();
        objects.add(pu);
        
        // create enemies lists'
        ArrayList<Player> enemies = new ArrayList<>();
        ArrayList<AiController> aiConList = new ArrayList<>();
        
        // create an enemy and its ai controller
        AiController aiCon = new AiController( new Player(ObjectType.ENEMY), objects, map ,player);
        aiConList.add(aiCon);
        enemies.add(aiCon.getAiPlayer() );
        enemies.get(0).setLocation(new Point2D(140, 100));
      
        // Add the enemies to the objects list
        objects.addAll(enemies);
        objects.add(player);
        System.out.println(objects);
        // generate a game state
        LinkedBlockingQueue deadPlayers = new LinkedBlockingQueue();
        ScoreBoard scoreboard = new ScoreBoard();
        GameType gameType = new FirstToXKillsGame(10);
        ClientGameState gameState = new ClientGameState(player, map, objects,deadPlayers, scoreboard, gameType);

        //This will be initialised on start of the game
        scoreboard.initialise(gameState.getAllPlayers());
        // start the engine.ai
        startAi(aiConList);
        
        return gameState;
    }

    //receive the number of enemy from controller to initialise engine.ai enm

    public static ClientGameState createDemoGamestateSample(int num_enm) {

        //initialise map location
        Rectangle map = new Rectangle(2000, 2000);

        // create player
        Player player = new Player(ObjectType.PLAYER);
        player.setId(4);
        Point2D playerStartLocation = new Point2D(500, 500);
        player.setLocation(playerStartLocation);


        //add the 1 power up to the objects list
        ArrayList<PhysicsObject> objects = new ArrayList<PhysicsObject>();


        // initialise enemies
        ArrayList<Player> enemies = new ArrayList<>();

        ArrayList<AiController> aiConList = new ArrayList<>();

        // Add the enemies to the objects list


        for (int i = 0; i < num_enm; i++) {
            AiController aiCon = new AiController( new Player(ObjectType.ENEMY), objects, map, player );
            aiConList.add(aiCon);
            enemies.add(aiCon.getAiPlayer() );
        }
        for (int i = 0; i < num_enm; i++) {

            if(i == 0) {
                enemies.get(i).setLocation(new Point2D(map.getWidth() - map.getWidth()/10, map.getHeight() - map.getHeight()/10));
            } else if (i == 1) {
                enemies.get(i).setLocation(new Point2D(0 +  map.getWidth()/10, map.getHeight() - map.getHeight()/10));
            } else if (i == 2) {
                enemies.get(i).setLocation(new Point2D(0 + map.getWidth()/10, 0 + map.getHeight()/10));
            }

            enemies.get(i).setId(i);
        }
        LinkedBlockingQueue deadPlayers = new LinkedBlockingQueue();


        //Add the enemies to the objects list
        objects.addAll(enemies);
        objects.add(player);


        ScoreBoard scoreboard = new ScoreBoard();
        // First to 10 kills
        GameType gameType = new FirstToXKillsGame(3);
        ClientGameState gameState = new ClientGameState(player, map, objects,deadPlayers, scoreboard, gameType);
        scoreboard.initialise(gameState.getAllPlayers());

        startAi(aiConList);


        return gameState;
    }
    
    private static void startAi(ArrayList<AiController> aiConList) {
    	for (AiController aiCon: aiConList)
    		aiCon.startEasyAi();
    }
}
