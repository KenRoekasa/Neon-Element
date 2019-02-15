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

        


        //add the 1 power up to the objects list
        ArrayList<PhysicsObject> objects = new ArrayList<PhysicsObject>();
        //TODO: Remove
        //add a powerup
        PowerUp pu = new PowerUp();

        objects.add(pu);
        
        
        // initialise enemies
        ArrayList<Player> enemies = new ArrayList<>();
        Player players[] = {player};
        PowerUp pus [] = {pu};
        
        ArrayList<AiController> aiConList = new ArrayList<>();
        AiController aiCon = new AiController( new Player(ObjectType.ENEMY),players, objects, map );
        
        enemies.add(aiCon.getAiPlayer() );
        
        enemies.get(0).setLocation(new Point2D(140, 100));
        //Add the enemies to the objects list
        objects.addAll(enemies);
        ClientGameState gameState = new ClientGameState(player, enemies, map, objects);

        startAi(aiConList);
        
        return gameState;
    }
    
    private static void startAi(ArrayList<AiController> aiConList) {
    	for (AiController aiCon: aiConList)
    		aiCon.startBasicAI();
    }
}
