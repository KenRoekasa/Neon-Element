package server;

import engine.model.GameState;
import engine.model.Map;
import engine.model.ScoreBoard;
import engine.ai.controller.AiControllersManager;
import engine.entities.*;
import engine.model.GameType;
import networking.Constants;

import java.util.ArrayList;

public class ServerGameState extends GameState {

    private int numPlayers;

    private boolean isStarted;

    public ServerGameState(Map map, ArrayList<PhysicsObject> objects, ScoreBoard scoreBoard, GameType gameType, AiControllersManager aiConMan, int numPlayers){
        super(map,objects, scoreBoard, gameType,aiConMan);
        this.startTime = System.currentTimeMillis();
        this.numPlayers = numPlayers;
    }

    public int getNumPlayers() {
        return this.numPlayers;
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
