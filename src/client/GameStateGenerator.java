package client;

import entities.Enemy;
import entities.PhysicsObject;
import entities.Player;
import entities.PowerUp;
import enums.ObjectType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class GameStateGenerator {

    public static GameState createDemoGamestate() {

        //initialise map location
        Rectangle map = new Rectangle(2000, 2000);

        // create player
        Player player = new Player(ObjectType.PLAYER);
        Point2D playerStartLocation = new Point2D(500, 500);
        player.setLocation(playerStartLocation);

        // initialise enemies
        ArrayList<Enemy> enemies = new ArrayList<>();
        enemies.add(new Enemy());
        enemies.get(0).setLocation(new Point2D(200, 200));



        //add the 1 power up to the objects list
        ArrayList<PhysicsObject> objects = new ArrayList<PhysicsObject>();
        //TODO: Remove
        //add a powerup
        PowerUp pu = new PowerUp();
        pu.setLocation(new Point2D(pu.getWidth()/2f,pu.getWidth()/2f));

        objects.add(pu);
        //Add the enemies to the objects list
        objects.addAll(enemies);


        GameState gameState = new GameState(player, enemies, map, objects);

        return gameState;
    }
}
