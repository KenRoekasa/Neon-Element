package client;

import entities.PhysicsObject;
import entities.Player;
import entities.PowerUp;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class GameStateGenerator {

    public static GameState createDemoGamestate() {

        //initialise map location
        Rectangle map = new Rectangle(1500, 1500);

        // create player
        Player player = new Player();
        Point2D playerStartLocation = new Point2D(500, 500);
        player.setLocation(playerStartLocation);

        // initialise enemies
        ArrayList<Player> enemies = new ArrayList<>();
        enemies.add(new Player());
        enemies.get(0).setLocation(new Point2D(140, 100));


        //add the 1 power up to the objects list
        ArrayList<PhysicsObject> objects = new ArrayList<PhysicsObject>();
        //TODO: Remove
        //add a powerup
        PowerUp pu = new PowerUp(player);

        objects.add(pu);
        //Add the enemies to the objects list
        objects.addAll(enemies);


        GameState gameState = new GameState(player, enemies, map, objects);

        return gameState;
    }
}
