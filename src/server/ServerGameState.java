package server;

import entities.BasicEnemy;
import entities.PhysicsObject;
import entities.Player;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class ServerGameState {
    
    private final int MAX_PLAYERS = 10;

    private boolean isStarted;
    private ArrayList<Player> players;
    private ArrayList<BasicEnemy> ais;
    private Rectangle map;
    private ArrayList<PhysicsObject> objects;

    public ServerGameState(ArrayList<Player> players, ArrayList<BasicEnemy> ais, Rectangle map, ArrayList<PhysicsObject> objects){
        this.players = players;
        this.ais = ais;
        this.map = map;
        this.objects = objects;
    }

    public Rectangle getMap() {
        return map;
    }

    public void setMap(Rectangle map) {
        this.map = map;
    }

    public ArrayList<BasicEnemy> getAis() {
        return ais;
    }

    public void setAis(ArrayList<BasicEnemy> ais) {
        this.ais = ais;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getMaxPlayers() {
        // TODO - use a variable from game setup to choose number
        return MAX_PLAYERS;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public ArrayList<PhysicsObject> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<PhysicsObject> objects) {
        this.objects = objects;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }
}
