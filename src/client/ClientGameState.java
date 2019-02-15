package client;

import entities.Enemy;
import entities.PhysicsObject;
import entities.Player;
import javafx.scene.shape.Rectangle;


import java.util.ArrayList;

public class ClientGameState {
    /** Me */
    private Player player;
    /** Opponents */
    private ArrayList<Player> players;
    /** AIs */
    private ArrayList<Enemy> enemies;
    private Rectangle map;
    private ArrayList<PhysicsObject> objects;

    public ClientGameState(Player player, ArrayList<Player> players, ArrayList<Enemy> enemies, Rectangle map, ArrayList<PhysicsObject> objects){
        this.player = player;
        this.players = players;
        this.enemies = enemies;
        this.map = map;
        this.objects = objects;
        
    }

    public void start() {
    	for (Enemy enemy : enemies) {
        	enemy.startBasicAI();
		}
    }
    
    public ArrayList<PhysicsObject> getEntities(){

        ArrayList<PhysicsObject> ents = new ArrayList<>(objects);
        ents.add(player);

        return ents;
    }

    public Rectangle getMap() {
        return map;
    }

    public void setMap(Rectangle map) {
        this.map = map;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(ArrayList<Enemy> enemies) {
        this.enemies = enemies;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<PhysicsObject> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<PhysicsObject> objects) {
        this.objects = objects;
    }
}
