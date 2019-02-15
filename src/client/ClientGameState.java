package client;

import engine.GameState;
import entities.PhysicsObject;
import entities.Player;
import javafx.scene.shape.Rectangle;


import java.util.ArrayList;

public class ClientGameState extends GameState {
    private Player player;
    private ArrayList<Player> enemies;


    public ClientGameState(Player player, ArrayList<Player> enemies, Rectangle map, ArrayList<PhysicsObject> objects){
        this.player = player;
        this.enemies = enemies;
        this.map = map;
        this.objects = objects;
        
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
