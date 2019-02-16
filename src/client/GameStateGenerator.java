package client;


import entities.Character;
import entities.PhysicsObject;
import entities.Player;
import entities.PowerUp;
import enums.ObjectType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

import ai.AiController;

public class GameStateGenerator {

    public static ClientGameState createDemoGamestate() {

        //initialise map location
        Rectangle map = new Rectangle(2000, 2000);

        // create player
        Player player = new Player(ObjectType.PLAYER);
        Point2D playerStartLocation = new Point2D(500, 500);
        player.setLocation(playerStartLocation);
	    Player players[] = {player};
        
        // create object list
        ArrayList<PhysicsObject> objects = new ArrayList<PhysicsObject>();
        
        // create power up
        PowerUp pu = new PowerUp();
        objects.add(pu);
        
        // create enemies lists'
        ArrayList<Player> enemies = new ArrayList<>();
        ArrayList<AiController> aiConList = new ArrayList<>();
        
        // create an enemy and its ai controller
        AiController aiCon = new AiController( new Player(ObjectType.ENEMY),players, objects, map );
        aiConList.add(aiCon);
        enemies.add(aiCon.getAiPlayer() );
        enemies.get(0).setLocation(new Point2D(140, 100));
      
        // Add the enemies to the objects list
        objects.addAll(enemies);
        
        // generate a game state
        ClientGameState gameState = new ClientGameState(player, enemies, map, objects);

        // start the ai
        startAi(aiConList);
        
        return gameState;
    }
    
    private static void startAi(ArrayList<AiController> aiConList) {
    	for (AiController aiCon: aiConList)
    		aiCon.startBasicAI();
    }
}
