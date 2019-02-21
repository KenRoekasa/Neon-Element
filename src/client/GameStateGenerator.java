package client;


import engine.ScoreBoard;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.entities.PowerUp;
import engine.enums.ObjectType;
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
        player.setID(100);
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
        ClientGameState gameState = new ClientGameState(player, map, objects,deadPlayers, scoreboard);

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
        player.setID(4);
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
            enemies.get(i).setLocation(new Point2D(140 + 200 * i, 100));
            enemies.get(i).setID(i);
        }
        LinkedBlockingQueue deadPlayers = new LinkedBlockingQueue();


        //Add the enemies to the objects list
        objects.addAll(enemies);
        objects.add(player);


        System.out.println(objects);
        ScoreBoard scoreboard = new ScoreBoard();
        ClientGameState gameState = new ClientGameState(player, map, objects,deadPlayers, scoreboard);
        scoreboard.initialise(gameState.getAllPlayers());

        startAi(aiConList);


        return gameState;
    }
    
    private static void startAi(ArrayList<AiController> aiConList) {
    	for (AiController aiCon: aiConList)
    		aiCon.startEasyAi();
    }
}
