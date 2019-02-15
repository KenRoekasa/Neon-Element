package server;

import engine.GameState;
import entities.*;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class ServerGameState extends GameState {
    
    private final int MAX_PLAYERS = 10;

    private boolean isStarted;
    private ArrayList<Player> players;
    private ArrayList<Player> ais;

    private ArrayList<PhysicsObject> objects;

    public ServerGameState(ArrayList<Player> players, ArrayList<Player> ais, Rectangle map, ArrayList<PhysicsObject> objects){
        this.players = players;
        this.ais = ais;
        this.map = map;
        this.objects = objects;
    }


    public ArrayList<Player> getAis() {
        return ais;
    }

    public void setAis(ArrayList<Player> ais) {
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


    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }
}
