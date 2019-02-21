package client;


import engine.ScoreBoard;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.entities.PowerUp;
import engine.enums.ObjectType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

import engine.ai.AiController;

public class GameStateGenerator {

    public static ClientGameState createDemoGamestate() {
    	System.out.println("generating game state");
        //initialise map location
        Rectangle map = new Rectangle(2000, 2000);

        // create player
        Player player = new Player(ObjectType.PLAYER);
        Point2D playerStartLocation = new Point2D(500, 500);
        player.setLocation(playerStartLocation);
        
        // create object list
        ArrayList<PhysicsObject> objects = new ArrayList<PhysicsObject>();
        
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
        
        // generate a game state
        ArrayList<Player> deadPlayers = new ArrayList<>();
        ScoreBoard scoreboard = new ScoreBoard(enemies.size()+1);
        ClientGameState gameState = new ClientGameState(player, enemies, map, objects,deadPlayers, scoreboard);

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
        Point2D playerStartLocation = new Point2D(500, 500);
        player.setLocation(playerStartLocation);


        //add the 1 power up to the objects list
        ArrayList<PhysicsObject> objects = new ArrayList<PhysicsObject>();
        //TODO: Remove
        //add a powerup
        PowerUp pu = new PowerUp();

        objects.add(pu);

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
        }
        ArrayList<Player> deadPlayers = new ArrayList<>();


        //Add the enemies to the objects list
        objects.addAll(enemies);
        ScoreBoard scoreboard = new ScoreBoard(enemies.size()+1);
        ClientGameState gameState = new ClientGameState(player, enemies, map, objects,deadPlayers, scoreboard);

        startAi(aiConList);


        return gameState;
    }
    
    private static void startAi(ArrayList<AiController> aiConList) {
    	for (AiController aiCon: aiConList)
    		aiCon.startEasyAi();
    }
}
