package server;

import engine.GameState;
import engine.ScoreBoard;
import engine.entities.*;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerGameState extends GameState {
    
    private final int MAX_PLAYERS = 10;

    private boolean isStarted;

    public ServerGameState(Rectangle map, LinkedBlockingQueue<Player> deadPlayers, ArrayList<PhysicsObject> objects, ScoreBoard scoreBoard){
        super(map,objects,deadPlayers,scoreBoard);
        this.startTime = System.currentTimeMillis();
    }

    public int getMaxPlayers() {
        // TODO - use a variable from game setup to choose number
        return MAX_PLAYERS;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.allPlayers = players;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }
}
