package client;

import engine.GameState;
import entities.Enemy;
import entities.PhysicsObject;
import entities.Player;
import entities.Character;
import enums.ObjectType;
import javafx.scene.shape.Rectangle;


import java.util.ArrayList;


public class ClientGameState extends GameState {
    private Player player;
  
    private ArrayList<Player> enemies;
    private Rectangle map;
    private ArrayList<PhysicsObject> objects;


    public ClientGameState(Player player, ArrayList<Player> enemies2, Rectangle map, ArrayList<PhysicsObject> objects){
        this.player = player;
        this.enemies = enemies2;
        this.map = map;
        this.objects = objects;
        
    }

    public void start() {
    	for (Character enemy : enemies) {
    	    if (enemy.getTag() == ObjectType.ENEMY) {
    	        ((Enemy) enemy).startBasicAI();
    	    }
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

    public ArrayList<Player> getEnemies() {
        return enemies;
    }

    public void setEnemies(ArrayList<Player> enemies) {
        this.enemies = enemies;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}
