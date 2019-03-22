package server;

import engine.model.GameState;
import engine.model.Map;
import engine.model.ScoreBoard;
import engine.ai.controller.AiControllersManager;
import engine.entities.*;
import engine.model.GameType;

import java.util.ArrayList;

public class ServerGameState extends GameState {

    private final int MAX_PLAYERS = 10;

    private boolean isStarted;

    public ServerGameState(Map map, ArrayList<PhysicsObject> objects, ScoreBoard scoreBoard, GameType gameType, AiControllersManager aiConMan){
        super(map,objects, scoreBoard, gameType,aiConMan);
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
